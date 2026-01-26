"""
ATS Quick Scan - FastAPI Backend
Main application entry point
"""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import uvicorn

from routers import scan

app = FastAPI(
    title="ATS Quick Scan API",
    description="Instant ATS compatibility check (estimate only)",
    version="1.0.0",
)

# CORS configuration - allow all origins for simplicity
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register routers
app.include_router(scan.router, prefix="/api")

@app.get("/")
async def root():
    """Health check endpoint"""
    return {"status": "ok", "message": "ATS Quick Scan API is running"}

@app.get("/health")
async def health():
    """Health check endpoint"""
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
