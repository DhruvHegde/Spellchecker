#  Android UI Spell Checker via OCR Automation

This project integrates Java-based Appium UI automation with a Python OCR pipeline to validate text in Android apps.
It captures screenshots during automated UI interactions and uses Tesseract + OpenCV for text extraction, followed by LanguageTool spell-checking to identify potential spelling issues.

---

## Key Features
- Automated app interaction using Java + Appium
- Captures screenshots after each UI step
- Performs OCR on screenshots using Tesseract
- Detects and highlights spelling errors via LanguageTool
- Dynamically displays filtered words and positions
- Supports multiple image inputs from Java workflow

---

##  Technologies Used
- Java, Appium, Selenium WebDriver
- Python 3, OpenCV, pytesseract
- LanguageTool (via `language_tool_python`)

---

---

## How It Works
1. Java Appium launches the Android app and navigates UI flows.
2. After each interaction, a screenshot is captured (`ss_<timestamp>.png`).
3. Java invokes the Python OCR script with the screenshot path.
4. Python:
   - Extracts text using Tesseract OCR
   - Filters based on confidence, size, and clarity
   - Checks spelling using LanguageTool
   - Highlights and prints errors with bounding box data
5. Annotated image is displayed and outputs are printed to terminal.

   
---

##  Prerequisites
- Android emulator or real device
- Appium server running locally
- Python 3, OpenCV, pytesseract, language_tool_python
- Tesseract OCR installed and in PATH

Install Python dependencies:
```bash
pip install opencv-python pytesseract language-tool-python


