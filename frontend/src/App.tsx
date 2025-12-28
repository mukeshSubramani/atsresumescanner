import { useState } from 'react'
import FileUpload from './components/FileUpload'
import TextArea from './components/TextArea'
import ResultsPanel from './components/ResultsPanel'
import FindingsList from './components/FindingsList'
import { scanResume } from './api/scanApi'
import { ScanResult } from './types'

function App() {
  const [resumeFile, setResumeFile] = useState<File | null>(null)
  const [resumeText, setResumeText] = useState('')
  const [jobDescription, setJobDescription] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [result, setResult] = useState<ScanResult | null>(null)

  const handleScan = async () => {
    setError(null)
    setResult(null)

    // Validation
    if (!jobDescription.trim()) {
      setError('Job description is required')
      return
    }

    if (!resumeFile && !resumeText.trim()) {
      setError('Please provide either a resume file or paste resume text')
      return
    }

    setLoading(true)

    try {
      const scanResult = await scanResume(resumeFile, resumeText, jobDescription)
      setResult(scanResult)
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'An error occurred while scanning the resume'
      setError(errorMessage)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
          <h1 className="text-3xl font-bold text-gray-900">ATS Quick Scan</h1>
          <p className="mt-1 text-sm text-gray-600">
            Instant ATS compatibility check (estimate only)
          </p>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8">
        {/* Input Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
          {/* Left Column - Resume Input */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Resume Input</h2>
            
            <FileUpload
              file={resumeFile}
              onFileChange={setResumeFile}
            />

            <div className="my-4 text-center text-gray-500 text-sm">OR</div>

            <TextArea
              value={resumeText}
              onChange={setResumeText}
              placeholder="Paste your resume text here..."
              label="Resume Text"
              rows={10}
            />

            <p className="mt-2 text-xs text-gray-500">
              Note: If both are provided, uploaded file takes priority
            </p>
          </div>

          {/* Right Column - Job Description */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">Job Description</h2>
            
            <TextArea
              value={jobDescription}
              onChange={setJobDescription}
              placeholder="Paste the job description here..."
              label="Job Description"
              rows={20}
              required
            />
          </div>
        </div>

        {/* Action Button */}
        <div className="flex justify-center mb-8">
          <button
            onClick={handleScan}
            disabled={loading}
            className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 text-white font-semibold py-3 px-8 rounded-lg shadow-md transition-colors duration-200"
          >
            {loading ? (
              <span className="flex items-center">
                <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                Scanning...
              </span>
            ) : (
              'Scan Resume'
            )}
          </button>
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-8">
            <p className="font-medium">Error</p>
            <p className="text-sm">{error}</p>
          </div>
        )}

        {/* Results Section */}
        {result && (
          <>
            <ResultsPanel result={result} />
            <FindingsList findings={result.findings} />
          </>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-gray-200 mt-16">
        <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
          <p className="text-sm text-gray-600 text-center">
            <strong>Disclaimer:</strong> This tool provides an estimate only. No guarantee of ATS outcomes.
            Files are processed in-memory and are not stored.
          </p>
        </div>
      </footer>
    </div>
  )
}

export default App
