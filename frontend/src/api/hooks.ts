import { useMutation, useQuery } from '@tanstack/react-query';
import api from './client';
import type { AuthResponse, ResumeResponse, ScanResponse, MarketRoleTemplate } from './types';

export const useLogin = () =>
  useMutation<AuthResponse, Error, { email: string; password: string }>(async (body) => {
    const { data } = await api.post('/api/auth/login', body);
    return data;
  });

export const useRegister = () =>
  useMutation<AuthResponse, Error, { email: string; password: string }>(async (body) => {
    const { data } = await api.post('/api/auth/register', body);
    return data;
  });

export const useMe = () =>
  useQuery({
    queryKey: ['me'],
    queryFn: async () => {
      const { data } = await api.get('/api/me');
      return data;
    },
    enabled: !!localStorage.getItem('token'),
  });

export const useResumes = () =>
  useQuery({
    queryKey: ['resumes'],
    queryFn: async () => {
      const { data } = await api.get('/api/resumes');
      return data as ResumeResponse[];
    },
  });

export const useUploadResume = () =>
  useMutation(async (file: File) => {
    const form = new FormData();
    form.append('file', file);
    const { data } = await api.post('/api/resumes/upload', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return data;
  });

export const useScans = () =>
  useQuery({
    queryKey: ['scans'],
    queryFn: async () => {
      const { data } = await api.get('/api/scans');
      return data as ScanResponse[];
    },
  });

export const useScanById = (id?: string) =>
  useQuery({
    queryKey: ['scan', id],
    queryFn: async () => {
      if (!id) return null;
      const { data } = await api.get(`/api/scans/${id}`);
      return data;
    },
    enabled: !!id,
  });

export const useMarketRoles = () =>
  useQuery({
    queryKey: ['market-roles'],
    queryFn: async () => {
      const { data } = await api.get('/api/market/roles');
      return data as MarketRoleTemplate[];
    },
  });

export const useJdScan = () =>
  useMutation(async (body: { resumeId: number; jdText: string; title?: string; company?: string; location?: string }) => {
    const { data } = await api.post('/api/scans/jd-match', body);
    return data;
  });

export const useMarketScan = () =>
  useMutation(async (body: { resumeId: number; roleKey: string; location?: string }) => {
    const { data } = await api.post('/api/scans/market-trend', body);
    return data;
  });
