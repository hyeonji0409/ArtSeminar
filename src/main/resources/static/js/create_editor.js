const importMap = {
    imports: {
        "ckeditor5": "https://cdn.ckeditor.com/ckeditor5/43.2.0/ckeditor5.js",
        "ckeditor5/": "https://cdn.ckeditor.com/ckeditor5/43.2.0/"
    }
};

const importMapScript = document.createElement("script");
importMapScript.type = "importmap";
importMapScript.textContent = JSON.stringify(importMap);
document.head.appendChild(importMapScript);
