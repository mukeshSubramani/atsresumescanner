"""
Findings service - generates resume findings and suggestions
"""
from typing import Dict, List, Any
import re

class FindingsService:
    """Service for generating resume findings and suggestions"""
    
    # Common resume sections
    COMMON_SECTIONS = ['experience', 'skills', 'education']
    
    def generate_findings(
        self,
        resume_content: str,
        missing_keywords: List[str],
        word_count: int
    ) -> Dict[str, Any]:
        """
        Generate findings for the resume
        
        Args:
            resume_content: Original resume text
            missing_keywords: List of missing keywords
            word_count: Number of words in resume
            
        Returns:
            Dictionary with missingSkills, formattingWarnings, and suggestions
        """
        missing_skills = self._extract_technical_skills(missing_keywords)
        formatting_warnings = self._check_formatting(resume_content, word_count)
        suggestions = self._generate_suggestions()
        
        return {
            "missingSkills": missing_skills,
            "formattingWarnings": formatting_warnings,
            "suggestions": suggestions
        }
    
    def _extract_technical_skills(self, missing_keywords: List[str]) -> List[str]:
        """
        Extract technical skills from missing keywords
        
        Uses basic heuristic: length > 2 and alphanumeric
        """
        skills = []
        for keyword in missing_keywords:
            # Basic heuristic: technical terms are usually > 2 chars and alphanumeric
            if len(keyword) > 2 and any(c.isalnum() for c in keyword):
                skills.append(keyword)
        
        return skills
    
    def _check_formatting(self, resume_text: str, word_count: int) -> List[str]:
        """Check for formatting issues"""
        warnings = []
        
        # Check length
        if word_count < 250:
            warnings.append("Resume is shorter than 250 words")
        elif word_count > 1200:
            warnings.append("Resume is longer than 1200 words")
        
        # Check for common sections
        resume_lower = resume_text.lower()
        missing_sections = []
        
        for section in self.COMMON_SECTIONS:
            if section not in resume_lower:
                missing_sections.append(section.capitalize())
        
        if missing_sections:
            warnings.append(f"Missing common sections: {', '.join(missing_sections)}")
        
        return warnings
    
    def _generate_suggestions(self) -> List[str]:
        """Generate improvement suggestions"""
        return [
            "Add missing keywords naturally in Skills and Experience sections",
            "Mirror important tools and technologies from the job description",
            "Ensure clear section headings for ATS readability"
        ]
