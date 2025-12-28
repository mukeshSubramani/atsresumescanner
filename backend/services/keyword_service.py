"""
Keyword service - extracts and matches keywords
"""
from typing import List, Tuple
from collections import Counter

class KeywordService:
    """Service for keyword extraction and matching"""
    
    # Common English stopwords to exclude
    STOPWORDS = {
        'a', 'an', 'and', 'are', 'as', 'at', 'be', 'by', 'for', 'from', 'has', 'he',
        'in', 'is', 'it', 'its', 'of', 'on', 'that', 'the', 'to', 'was', 'will',
        'with', 'this', 'but', 'or', 'not', 'have', 'had', 'has', 'do', 'does',
        'did', 'been', 'being', 'can', 'could', 'would', 'should', 'may', 'might',
        'must', 'shall', 'we', 'you', 'they', 'i', 'me', 'my', 'your', 'their',
        'our', 'his', 'her', 'all', 'any', 'each', 'every', 'some', 'such', 'which',
        'who', 'whom', 'what', 'where', 'when', 'why', 'how', 'am', 'so', 'than',
        'too', 'very', 'if', 'then', 'else', 'while', 'there', 'here'
    }
    
    def extract_keywords(self, text: str, top_n: int = 30) -> List[str]:
        """
        Extract top N keywords from text
        
        Args:
            text: Normalized text
            top_n: Number of top keywords to return
            
        Returns:
            List of keywords
        """
        # Tokenize (split by spaces since text is already normalized)
        tokens = text.split()
        
        # Filter out stopwords and short tokens
        filtered_tokens = [
            token for token in tokens
            if token not in self.STOPWORDS and len(token) > 2
        ]
        
        # Count frequencies
        counter = Counter(filtered_tokens)
        
        # Get top N most common
        top_keywords = [word for word, count in counter.most_common(top_n)]
        
        return top_keywords
    
    def match_keywords(self, keywords: List[str], resume_text: str) -> Tuple[List[str], List[str]]:
        """
        Match keywords against resume text
        
        Args:
            keywords: List of keywords to match
            resume_text: Normalized resume text
            
        Returns:
            Tuple of (matched_keywords, missing_keywords)
        """
        matched = []
        missing = []
        
        for keyword in keywords:
            # Check if keyword appears in resume
            if keyword in resume_text:
                matched.append(keyword)
            else:
                missing.append(keyword)
        
        return matched, missing
