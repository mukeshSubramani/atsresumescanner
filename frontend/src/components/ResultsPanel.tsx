import { ScanResult } from '../types';

interface ResultsPanelProps {
  result: ScanResult;
}

const ResultsPanel = ({ result }: ResultsPanelProps) => {
  const getScoreColor = (score: number) => {
    if (score >= 70) return 'text-green-600';
    if (score >= 40) return 'text-yellow-600';
    return 'text-red-600';
  };

  const getScoreBgColor = (score: number) => {
    if (score >= 70) return 'bg-green-50 border-green-200';
    if (score >= 40) return 'bg-yellow-50 border-yellow-200';
    return 'bg-red-50 border-red-200';
  };

  return (
    <div className="bg-white rounded-lg shadow p-6 mb-8">
      <h2 className="text-2xl font-semibold text-gray-900 mb-6">Scan Results</h2>

      {/* Overall Score */}
      <div className={`border-2 rounded-lg p-6 mb-6 ${getScoreBgColor(result.overallScore)}`}>
        <div className="text-center">
          <p className="text-sm font-medium text-gray-600 mb-2">Overall Match Score</p>
          <p className={`text-6xl font-bold ${getScoreColor(result.overallScore)}`}>
            {result.overallScore}
          </p>
          <p className="text-2xl font-medium text-gray-600 mt-1">/ 100</p>
        </div>
      </div>

      {/* Keywords Section */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Matched Keywords */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-3">
            Matched Keywords
            <span className="ml-2 text-sm font-normal text-gray-500">
              ({result.matchedKeywords.length})
            </span>
          </h3>
          <div className="flex flex-wrap gap-2">
            {result.matchedKeywords.length > 0 ? (
              result.matchedKeywords.map((keyword, index) => (
                <span
                  key={index}
                  className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-green-100 text-green-800"
                >
                  {keyword}
                </span>
              ))
            ) : (
              <p className="text-gray-500 text-sm">No matched keywords</p>
            )}
          </div>
        </div>

        {/* Missing Keywords */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-3">
            Missing Keywords
            <span className="ml-2 text-sm font-normal text-gray-500">
              ({result.missingKeywords.length})
            </span>
          </h3>
          <div className="flex flex-wrap gap-2">
            {result.missingKeywords.length > 0 ? (
              result.missingKeywords.map((keyword, index) => (
                <span
                  key={index}
                  className="inline-flex items-center px-3 py-1 rounded-full text-sm font-medium bg-red-100 text-red-800"
                >
                  {keyword}
                </span>
              ))
            ) : (
              <p className="text-gray-500 text-sm">All keywords matched!</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ResultsPanel;
