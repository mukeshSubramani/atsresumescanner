import { Findings } from '../types';

interface FindingsListProps {
  findings: Findings;
}

const FindingsList = ({ findings }: FindingsListProps) => {
  return (
    <div className="bg-white rounded-lg shadow p-6">
      <h2 className="text-2xl font-semibold text-gray-900 mb-6">Findings</h2>

      <div className="space-y-6">
        {/* Missing Skills */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
            <svg className="h-5 w-5 text-orange-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            Missing Skills
          </h3>
          {findings.missingSkills.length > 0 ? (
            <ul className="list-disc list-inside space-y-1 text-gray-700">
              {findings.missingSkills.map((skill, index) => (
                <li key={index}>{skill}</li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500">No missing skills identified</p>
          )}
        </div>

        {/* Formatting Warnings */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
            <svg className="h-5 w-5 text-yellow-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            Formatting Warnings
          </h3>
          {findings.formattingWarnings.length > 0 ? (
            <ul className="list-disc list-inside space-y-1 text-gray-700">
              {findings.formattingWarnings.map((warning, index) => (
                <li key={index}>{warning}</li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500">No formatting issues detected</p>
          )}
        </div>

        {/* Suggestions */}
        <div>
          <h3 className="text-lg font-semibold text-gray-900 mb-3 flex items-center">
            <svg className="h-5 w-5 text-blue-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
            Improvement Suggestions
          </h3>
          {findings.suggestions.length > 0 ? (
            <ul className="list-disc list-inside space-y-1 text-gray-700">
              {findings.suggestions.map((suggestion, index) => (
                <li key={index}>{suggestion}</li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500">No suggestions at this time</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default FindingsList;
