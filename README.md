# ATS Quick Scan

A simple, elegant web application for instant ATS (Applicant Tracking System) compatibility checking. Upload your resume and paste a job description to get an immediate compatibility score with actionable feedback.

## Features

- 🚀 **Fast & Simple**: Single-page application with instant results
- 📄 **Multiple Input Methods**: Upload PDF/DOCX or paste plain text
- 🎯 **Keyword Matching**: Identifies matched and missing keywords
- 💡 **Actionable Insights**: Get formatting warnings and improvement suggestions
- 🔒 **Privacy-First**: All processing happens in-memory, no data stored
- 🐳 **Fully Containerized**: Easy deployment with Docker

## Tech Stack

### Backend
- Python 3.11
- FastAPI
- Uvicorn
- PyMuPDF (PDF text extraction)
- python-docx (DOCX text extraction)

### Frontend
- React 18
- TypeScript
- Vite
- Tailwind CSS
- Axios

## Prerequisites

### For Local Development
- Python 3.11+
- Node.js 20+
- npm

### For Docker Deployment
- Docker
- Docker Compose

## Quick Start

### Option 1: Using Docker (Recommended)

1. Clone the repository:
```bash
git clone https://github.com/mukeshSubramani/atsresumescanner.git
cd atsresumescanner
```

2. Start the application:
```bash
docker-compose up --build
```

3. Access the application:
   - Frontend: http://localhost:5173
   - Backend API: http://localhost:8000
   - API Documentation: http://localhost:8000/docs

### Option 2: Local Development

#### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Create a virtual environment:
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

3. Install dependencies:
```bash
pip install -r requirements.txt
```

4. Run the backend:
```bash
python main.py
# OR
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

Backend will be available at http://localhost:8000

#### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

Frontend will be available at http://localhost:5173

## Usage

1. **Input Your Resume**:
   - Upload a PDF or DOCX file, OR
   - Paste your resume text in the textarea
   - Note: If both are provided, the uploaded file takes priority

2. **Input Job Description**:
   - Paste the complete job description in the right textarea (required)

3. **Click "Scan Resume"**:
   - Wait for processing (usually takes a few seconds)

4. **Review Results**:
   - **Overall Score**: 0-100 compatibility score
   - **Matched Keywords**: Keywords found in your resume
   - **Missing Keywords**: Important keywords you should add
   - **Findings**:
     - Missing Skills: Technical skills not present in your resume
     - Formatting Warnings: Issues with resume length or structure
     - Suggestions: Actionable tips for improvement

## API Documentation

### POST /api/scan

Scan a resume against a job description.

**Request** (multipart/form-data):
- `resumeFile` (optional): PDF or DOCX file (max 5MB)
- `resumeText` (optional): Plain text resume
- `jobDescriptionText` (required): Job description text

**Response** (JSON):
```json
{
  "overallScore": 85,
  "matchedKeywords": ["python", "fastapi", "react"],
  "missingKeywords": ["docker", "kubernetes"],
  "findings": {
    "missingSkills": ["docker", "kubernetes"],
    "formattingWarnings": ["Resume is longer than 1200 words"],
    "suggestions": [
      "Add missing keywords naturally in Skills and Experience sections",
      "Mirror important tools and technologies from the job description",
      "Ensure clear section headings for ATS readability"
    ]
  }
}
```

### GET /health

Health check endpoint.

**Response**:
```json
{
  "status": "healthy"
}
```

## Environment Variables

### Backend
None required for basic operation.

### Frontend
- `VITE_API_URL`: Backend API URL (default: `http://localhost:8000`)

To set in development:
```bash
# Create frontend/.env file
echo "VITE_API_URL=http://localhost:8000" > frontend/.env
```

## Building for Production

### Backend
```bash
cd backend
docker build -t ats-backend .
docker run -p 8000:8000 ats-backend
```

### Frontend
```bash
cd frontend
npm run build
# Built files will be in dist/ directory
```

## Limitations

- **Scoring Algorithm**: Uses simple keyword frequency matching, not AI-powered semantic analysis
- **File Size**: Maximum 5MB for uploaded files
- **File Types**: Only PDF and DOCX supported
- **No Persistence**: Results are not saved; no database
- **No Authentication**: Open access for all users
- **Keyword Extraction**: Basic frequency-based extraction without context understanding

## Future Enhancements

Potential features for future releases (not currently implemented):

- 🤖 AI-powered semantic analysis using LLMs
- 📊 Market trend analysis and salary insights
- ✏️ AI-based resume rewriting suggestions
- 💾 Save scan history (requires database)
- 📑 PDF report export
- ⚖️ Skill weighting by job role
- 🔐 User authentication and profiles
- 📈 Resume scoring over time tracking
- 🌐 Multi-language support

## Project Structure

```
atsresumescanner/
├── backend/
│   ├── main.py                 # FastAPI application entry point
│   ├── routers/
│   │   └── scan.py            # Scan endpoint routes
│   ├── services/
│   │   ├── scan_service.py    # Main scanning orchestration
│   │   ├── keyword_service.py # Keyword extraction & matching
│   │   └── findings_service.py # Resume findings generation
│   ├── extractors/
│   │   └── text_extractor.py  # PDF/DOCX text extraction
│   ├── utils/
│   ├── requirements.txt       # Python dependencies
│   └── Dockerfile            # Backend container config
├── frontend/
│   ├── src/
│   │   ├── components/        # React components
│   │   │   ├── FileUpload.tsx
│   │   │   ├── TextArea.tsx
│   │   │   ├── ResultsPanel.tsx
│   │   │   └── FindingsList.tsx
│   │   ├── api/
│   │   │   └── scanApi.ts     # API client
│   │   ├── types.ts           # TypeScript types
│   │   ├── App.tsx           # Main application component
│   │   ├── main.tsx          # Application entry point
│   │   └── index.css         # Tailwind styles
│   ├── package.json          # Node dependencies
│   ├── vite.config.ts        # Vite configuration
│   ├── tailwind.config.ts    # Tailwind configuration
│   ├── nginx.conf            # Nginx configuration for production
│   └── Dockerfile            # Frontend container config
├── docker-compose.yml        # Multi-container orchestration
└── README.md                 # This file
```

## Troubleshooting

### Backend Issues

**Error: "Failed to extract text from PDF"**
- Ensure the PDF is not encrypted or password-protected
- Try a different PDF or use plain text input

**Error: "File size exceeds 5MB limit"**
- Compress your resume or convert to plain text

### Frontend Issues

**CORS errors**
- Ensure backend is running and CORS is enabled
- Check that `VITE_API_URL` points to the correct backend URL

**Build errors**
- Clear node_modules and reinstall: `rm -rf node_modules && npm install`
- Ensure Node.js version is 20+

### Docker Issues

**Port already in use**
- Change ports in docker-compose.yml
- Or stop other services using ports 8000 or 5173

**Container fails to start**
- Check logs: `docker-compose logs`
- Rebuild: `docker-compose up --build --force-recreate`

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Disclaimer

This tool provides an estimate only and should not be considered a guarantee of ATS performance. Actual ATS systems vary widely in their algorithms and requirements. Always tailor your resume for each specific job application.

## Support

For issues, questions, or suggestions, please open an issue on GitHub.
