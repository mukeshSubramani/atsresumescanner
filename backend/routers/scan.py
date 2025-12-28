"""
Scan router - handles resume scanning requests
"""
from fastapi import APIRouter, HTTPException, UploadFile, File, Form
from typing import Optional
import logging

from services.scan_service import ScanService

logger = logging.getLogger(__name__)
router = APIRouter(tags=["scan"])
scan_service = ScanService()

@router.post("/scan")
async def scan_resume(
    resumeFile: Optional[UploadFile] = File(None),
    resumeText: Optional[str] = Form(None),
    jobDescriptionText: str = Form(...)
):
    """
    Scan a resume against a job description.
    
    Args:
        resumeFile: Optional PDF or DOCX file
        resumeText: Optional plain text resume
        jobDescriptionText: Required job description text
        
    Returns:
        Scan results with score, keywords, and findings
    """
    try:
        # Validate inputs
        if not jobDescriptionText or not jobDescriptionText.strip():
            raise HTTPException(status_code=400, detail="Job description text is required")
        
        if not resumeFile and not resumeText:
            raise HTTPException(
                status_code=400,
                detail="Either resumeFile or resumeText must be provided"
            )
        
        # Process the resume
        result = await scan_service.scan_resume(
            resume_file=resumeFile,
            resume_text=resumeText,
            job_description_text=jobDescriptionText
        )
        
        return result
        
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"Error scanning resume: {str(e)}", exc_info=True)
        raise HTTPException(status_code=500, detail=f"Error processing request: {str(e)}")
