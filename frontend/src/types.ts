export interface ScanResult {
  overallScore: number;
  matchedKeywords: string[];
  missingKeywords: string[];
  findings: Findings;
}

export interface Findings {
  missingSkills: string[];
  formattingWarnings: string[];
  suggestions: string[];
}
