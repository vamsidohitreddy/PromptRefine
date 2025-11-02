import React from "react";
import PromptEditor from "./components/PromptEditor";

function App() {
  return (
    <div className="min-h-screen relative flex flex-col items-center justify-center text-white overflow-hidden bg-black">
      {/* 🌄 Subtle animated snowy mountain background */}
      <div className="absolute inset-0 bg-black">
        <svg
          className="absolute bottom-0 w-full h-[40vh] opacity-10 animate-[slowPan_25s_linear_infinite]"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1440 320"
          preserveAspectRatio="none"
        >
          <path
            fill="white"
            d="M0,256L80,240C160,224,320,192,480,176C640,160,800,160,960,165.3C1120,171,1280,181,1360,186.7L1440,192L1440,320L1360,320C1280,320,1120,320,960,320C800,320,640,320,480,320C320,320,160,320,80,320L0,320Z"
          ></path>
        </svg>

        {/* light snowfall effect */}
        <div className="absolute inset-0 overflow-hidden">
          {[...Array(30)].map((_, i) => (
            <div
              key={i}
              className="absolute w-1 h-1 bg-white rounded-full opacity-40 animate-[snowfall_15s_linear_infinite]"
              style={{
                top: `${Math.random() * 100}%`,
                left: `${Math.random() * 100}%`,
                animationDelay: `${Math.random() * 10}s`,
                animationDuration: `${10 + Math.random() * 10}s`,
              }}
            />
          ))}
        </div>
      </div>

      {/* 🖋 Title */}
      <h1 className="z-10 text-4xl font-semibold tracking-tight mb-10 text-center text-gray-200">
        PromptRefine
      </h1>

      {/* 🧠 Main Editor */}
      <div className="z-10 w-full max-w-3xl">
        <PromptEditor />
      </div>
    </div>
  );
}

export default App;



