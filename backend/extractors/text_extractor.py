"""
Text extractor - extracts text from PDF and DOCX files
"""
import io
import logging
from typing import Union

logger = logging.getLogger(__name__)

class TextExtractor:
    """Extracts text from various file formats"""
    
    def extract_from_pdf(self, content: bytes) -> str:
        """
        Extract text from PDF file using PyMuPDF
        
        Args:
            content: PDF file content as bytes
            
        Returns:
            Extracted text
        """
        try:
            import fitz  # PyMuPDF
            
            # Open PDF from bytes
            pdf_document = fitz.open(stream=content, filetype="pdf")
            
            text = ""
            for page_num in range(pdf_document.page_count):
                page = pdf_document[page_num]
                text += page.get_text()
            
            pdf_document.close()
            
            return text.strip()
            
        except Exception as e:
            logger.error(f"Error extracting text from PDF: {str(e)}")
            raise Exception(f"Failed to extract text from PDF: {str(e)}")
    
    def extract_from_docx(self, content: bytes) -> str:
        """
        Extract text from DOCX file using python-docx
        
        Args:
            content: DOCX file content as bytes
            
        Returns:
            Extracted text
        """
        try:
            from docx import Document
            
            # Open DOCX from bytes
            doc = Document(io.BytesIO(content))
            
            # Extract text from all paragraphs
            text = "\n".join([paragraph.text for paragraph in doc.paragraphs])
            
            return text.strip()
            
        except Exception as e:
            logger.error(f"Error extracting text from DOCX: {str(e)}")
            raise Exception(f"Failed to extract text from DOCX: {str(e)}")
