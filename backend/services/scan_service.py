"""
Scan service - orchestrates resume scanning logic
"""
from fastapi import UploadFile, HTTPException
from typing import Optional, Dict, Any
import logging

from extractors.text_extractor import TextExtractor
from services.keyword_service import KeywordService
from services.findings_service import FindingsService

logger = logging.getLogger(__name__)

class ScanService:
    """Main service for scanning resumes against job descriptions"""
    
    def __init__(self):
        self.text_extractor = TextExtractor()
        self.keyword_service = KeywordService()
        self.findings_service = FindingsService()
    
    async def scan_resume(
        self,
        resume_file: Optional[UploadFile],
        resume_text: Optional[str],
        job_description_text: str
    ) -> Dict[str, Any]:
        """
        Scan a resume against a job description
        
        Args:
            resume_file: Optional uploaded file (PDF or DOCX)
            resume_text: Optional plain text resume
            job_description_text: Job description text
            
        Returns:
            Dictionary containing scan results
        """
        # Extract resume text
        if resume_file:
            # File takes priority
            resume_content = await self._extract_from_file(resume_file)
        elif resume_text:
            resume_content = resume_text
        else:
            raise HTTPException(status_code=400, detail="No resume provided")
        
        # Normalize texts
        resume_normalized = self._normalize_text(resume_content)
        jd_normalized = self._normalize_text(job_description_text)
        
        # Extract keywords from job description
        jd_keywords = self.keyword_service.extract_keywords(jd_normalized)
        
        # Match keywords
        matched, missing = self.keyword_service.match_keywords(
            jd_keywords, resume_normalized
        )
        
        # Calculate score
        overall_score = self._calculate_score(matched, jd_keywords)
        
        # Generate findings
        findings = self.findings_service.generate_findings(
            resume_content=resume_content,
            missing_keywords=missing,
            word_count=len(resume_content.split())
        )
        
        return {
            "overallScore": overall_score,
            "matchedKeywords": matched,
            "missingKeywords": missing,
            "findings": findings
        }
    
    async def _extract_from_file(self, file: UploadFile) -> str:
        """Extract text from uploaded file"""
        # Validate file size (5MB max)
        content = await file.read()
        if len(content) > 5 * 1024 * 1024:
            raise HTTPException(status_code=400, detail="File size exceeds 5MB limit")
        
        # Reset file pointer
        await file.seek(0)
        
        # Extract text based on file type
        file_extension = file.filename.lower().split('.')[-1] if file.filename else ''
        
        if file_extension == 'pdf':
            text = self.text_extractor.extract_from_pdf(content)
        elif file_extension in ['docx', 'doc']:
            text = self.text_extractor.extract_from_docx(content)
        else:
            raise HTTPException(
                status_code=400,
                detail=f"Unsupported file type: .{file_extension}. Only PDF and DOCX are supported."
            )
        
        return text
    
    def _normalize_text(self, text: str) -> str:
        """Normalize text: lowercase, remove punctuation, collapse whitespace"""
        import re
        import string
        
        # Lowercase
        text = text.lower()
        
        # Remove punctuation but keep spaces
        text = text.translate(str.maketrans(string.punctuation, ' ' * len(string.punctuation)))
        
        # Collapse whitespace
        text = re.sub(r'\s+', ' ', text).strip()
        
        return text
    
    def _calculate_score(self, matched_keywords: list, all_keywords: list) -> int:
        """Calculate overall match score"""
        if not all_keywords:
            return 0
        
        coverage = len(matched_keywords) / len(all_keywords)
        return round(coverage * 100)
