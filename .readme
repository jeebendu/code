To migrate an existing React app using Create React App (CRA) to Vite, follow these steps:

✅ Step-by-Step Migration from CRA (react-scripts) to Vite
1. Install Vite and required dependencies
Delete react-scripts and install Vite:

bash
Copy
Edit
npm uninstall react-scripts
npm install --save-dev vite
Then install required Vite-compatible packages:

bash
Copy
Edit
npm install react react-dom
npm install --save-dev @vitejs/plugin-react
2. Update package.json scripts
Replace CRA scripts with Vite equivalents:

json
Copy
Edit
"scripts": {
  "dev": "vite",
  "build": "vite build",
  "preview": "vite preview"
}
3. Create vite.config.js
js
Copy
Edit
// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    open: true
  }
})
4. Reorganize the index.html file
Move index.html from public/ to root of project and update it like so:

html
Copy
Edit
<!-- index.html (root level) -->
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Vite React App</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.jsx"></script>
  </body>
</html>
5. Update entry point (typically src/index.js or src/index.jsx)
Vite uses ES Modules, so your main.jsx or main.tsx should already be compatible:

jsx
Copy
Edit
// src/main.jsx
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
)
6. Assets and Environment Variables
Assets: Move static assets from public/ to src/assets/ (or use public/ carefully).

Env variables: Rename .env to .env.local and:

CRA: REACT_APP_API_KEY

Vite: VITE_API_KEY

Update usages accordingly.

7. Remove unused CRA files
You can delete:

bash
Copy
Edit
rm -rf public/index.html src/reportWebVitals.js src/setupTests.js
(only if unused)

8. Run the app
bash
Copy
Edit
npm run dev
Your app should now run via Vite at http://localhost:5173

✅ Optional Tweaks
Alias setup (if using @/components style imports):

js
Copy
Edit
import path from 'path'
export default defineConfig({
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
})
CSS Modules / Sass: Vite supports these out of the box.
