import { useParams } from 'react-router-dom';
import Navbar from '../components/Navbar';
import { useScanById } from '../api/hooks';
import ScoreCard from '../components/ScoreCard';
import KeywordList from '../components/KeywordList';
import SuggestionsPanel from '../components/SuggestionsPanel';
import ScoreChart from '../components/ScoreChart';

export default function Results() {
  const { id } = useParams();
  const { data } = useScanById(id);
  const result = data?.result;

  return (
    <div>
      <Navbar />
      <div className="max-w-6xl mx-auto px-6 py-10 space-y-6">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold">Scan Result</h2>
          <a
            href={`data:application/json;charset=utf-8,${encodeURIComponent(JSON.stringify(result || {}, null, 2))}`}
            download={`scan-${id}.json`}
            className="px-4 py-2 rounded bg-slate-800 border border-slate-700 text-sm"
          >
            Download JSON
          </a>
        </div>
        {!result && <div className="text-slate-400">Loading...</div>}
        {result && (
          <>
            <div className="grid md:grid-cols-2 gap-4">
              <ScoreChart value={result.overallScore} />
              <div className="grid grid-cols-2 gap-3">
                <ScoreCard title="Keyword" value={result.keywordMatchScore} />
                <ScoreCard title="Skills" value={result.skillsMatchScore} />
                <ScoreCard title="Experience" value={result.experienceMatchScore} />
                <ScoreCard title="Formatting" value={result.formattingScore} />
              </div>
            </div>
            <div className="grid md:grid-cols-2 gap-4">
              <KeywordList title="Matched" items={result.keywordCoverage?.matchedKeywords || []} />
              <KeywordList title="Missing" items={result.keywordCoverage?.missingKeywords || []} />
            </div>
            <SuggestionsPanel suggestions={result.suggestions || []} />
          </>
        )}
      </div>
    </div>
  );
}
