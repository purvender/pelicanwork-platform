export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <h1 className="text-4xl font-bold mb-4">Pelicanwork</h1>
      <p className="text-xl text-gray-600">
        AI-enabled commerce knowledge platform
      </p>
      <div className="mt-8">
        <a
          href="http://localhost:8080/health"
          className="text-blue-500 hover:underline"
          target="_blank"
          rel="noopener noreferrer"
        >
          Backend Health Check →
        </a>
      </div>
    </main>
  );
}