export interface ResumeResponse {
  id: number;
  name: string;
  fileType: string;
  filePath: string;
  extractedText: string;
  uploadedAt: string;
}

export interface ScanResult {
  overallScore: number;
  keywordMatchScore: number;
  skillsMatchScore: number;
  experienceMatchScore: number;
  formattingScore: number;
  sectionsDetected: string[];
  keywordCoverage: {
    matchedKeywords: string[];
    missingKeywords: string[];
  };
  suggestions: Array<{
    severity: string;
    message: string;
  }>;
  extras: Record<string, unknown>;
}

export interface ScanResponse {
  id: number;
  scanType: string;
  result: ScanResult;
  createdAt: string;
}

export interface MarketRoleTemplate {
  key: string;
  title: string;
  seniority: string;
  location: string;
}

export interface AuthResponse {
  token: string;
  role: string;
}
