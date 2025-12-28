import axios from 'axios';
import { ScanResult } from '../types';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8000';

export const scanResume = async (
  resumeFile: File | null,
  resumeText: string,
  jobDescriptionText: string
): Promise<ScanResult> => {
  const formData = new FormData();

  if (resumeFile) {
    formData.append('resumeFile', resumeFile);
  }

  if (resumeText) {
    formData.append('resumeText', resumeText);
  }

  formData.append('jobDescriptionText', jobDescriptionText);

  try {
    const response = await axios.post<ScanResult>(`${API_URL}/api/scan`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    return response.data;
  } catch (error: any) {
    if (error.response?.data?.detail) {
      throw new Error(error.response.data.detail);
    }
    throw new Error('Failed to scan resume. Please try again.');
  }
};
