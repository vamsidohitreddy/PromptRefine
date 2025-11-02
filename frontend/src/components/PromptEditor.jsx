import { useState } from "react";
import axios from "axios";
import { marked } from "marked";

export default function PromptEditor() {
  const [prompt, setPrompt] = useState("");
  const [mode, setMode] = useState("rewrite");
  const [result, setResult] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleGenerate = async () => {
    if (!prompt.trim()) return;
    setLoading(true);
    try {
      const res = await axios.post("http://localhost:8080/api/rewrite", { prompt, mode });
      const text = res.data.output || "No output.";
      const options = text.split(/(?=\*\*Option\s+\d+:)/g).map((opt) => opt.trim());
      setResult(options.length ? options : [text]);
    } catch (e) {
      console.error(e);
      setResult(["Error fetching response"]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative bg-[#0f0f0f]/90 backdrop-blur-xl border border-gray-800 rounded-2xl p-8 shadow-[0_0_25px_rgba(255,255,255,0.05)] text-white">
      <h1 className="text-2xl font-semibold text-center mb-6 text-gray-300">
        AI Text Rewriter
      </h1>

      <textarea
        className="w-full p-4 rounded-xl bg-black border border-gray-800 text-gray-200 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-600 resize-none"
        rows="6"
        placeholder="Type or paste your text here..."
        value={prompt}
        onChange={(e) => setPrompt(e.target.value)}
      />

      <div className="flex flex-wrap gap-3 mt-4 justify-center">
        {["rewrite", "shorten", "lengthen", "casual", "formal"].map((m) => (
          <button
            key={m}
            onClick={() => setMode(m)}
            className={`px-4 py-2 rounded-full text-sm font-medium border transition-all duration-200 ${
              mode === m
                ? "bg-white text-black border-white"
                : "border-gray-700 text-gray-400 hover:bg-gray-800"
            }`}
          >
            {m.charAt(0).toUpperCase() + m.slice(1)}
          </button>
        ))}
      </div>

      <button
        onClick={handleGenerate}
        disabled={loading}
        className="w-full mt-6 bg-white text-black font-semibold py-3 rounded-xl hover:bg-gray-200 transition-all duration-200 disabled:opacity-50"
      >
        {loading ? "Processing..." : "Generate"}
      </button>

      {result.length > 0 && (
        <div className="mt-6 space-y-4">
          <h3 className="text-lg font-semibold border-b border-gray-800 pb-2 text-gray-300">
            Output
          </h3>
          {result.map((opt, i) => (
            <div
              key={i}
              className="p-4 bg-black/70 border border-gray-800 rounded-xl"
              dangerouslySetInnerHTML={{ __html: marked(opt) }}
            />
          ))}
        </div>
      )}
    </div>
  );
}



// import { useState } from "react";
// import axios from "axios";
// import { marked } from "marked"; // convert markdown (**bold**, etc.) to HTML

// export default function PromptEditor() {
//   const [prompt, setPrompt] = useState("");
//   const [mode, setMode] = useState("rewrite");
//   const [result, setResult] = useState([]);
//   const [loading, setLoading] = useState(false);

//   const handleGenerate = async () => {
//     if (!prompt.trim()) return;
//     setLoading(true);
//     try {
//       const res = await axios.post("http://localhost:8080/api/rewrite", { prompt, mode });
//       const text = res.data.output || "No output.";

//       // 🧩 Split into sections like "Option 1:", "Option 2:", etc.
//       const options = text.split(/(?=\*\*Option\s+\d+:)/g).map((opt) => opt.trim());

//       setResult(options.length ? options : [text]);
//     } catch (e) {
//       console.error(e);
//       setResult(["Error fetching response"]);
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <div className="bg-white shadow-lg rounded-2xl p-6 w-full max-w-2xl">
//       <textarea
//         className="w-full p-3 border rounded-lg focus:outline-none"
//         rows="5"
//         placeholder="Paste or type your prompt..."
//         value={prompt}
//         onChange={(e) => setPrompt(e.target.value)}
//       />

//       <div className="flex flex-wrap gap-2 mt-3">
//         {["rewrite", "shorten", "lengthen", "casual", "formal"].map((m) => (
//           <button
//             key={m}
//             onClick={() => setMode(m)}
//             className={`px-3 py-1 rounded-lg text-sm font-semibold ${
//               mode === m ? "bg-blue-500 text-white" : "bg-gray-200 hover:bg-gray-300"
//             }`}
//           >
//             {m}
//           </button>
//         ))}
//       </div>

//       <button
//         onClick={handleGenerate}
//         disabled={loading}
//         className="mt-4 bg-blue-600 text-white px-5 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
//       >
//         {loading ? "Processing..." : "Generate"}
//       </button>

//       {result.length > 0 && (
//         <div className="mt-5 space-y-4">
//           <h3 className="font-semibold mb-2 text-lg">Output:</h3>
//           {result.map((opt, i) => (
//             <div
//               key={i}
//               className="p-4 bg-gray-50 border rounded-lg shadow-sm"
//               dangerouslySetInnerHTML={{ __html: marked(opt) }}
//             />
//           ))}
//         </div>
//       )}
//     </div>
//   );
// }

