import sys
sys.stdout.reconfigure(encoding='utf-8')

import cv2
import pytesseract
import language_tool_python

# Check command line arguments
if len(sys.argv) < 2:
    print("Usage: python test1.py <image1> [<image2> ...]")
    sys.exit(1)

# Support multiple image paths
image_paths = sys.argv[1:]

# Initialize LanguageTool for English
tool = language_tool_python.LanguageTool('en-US')

for image_path in image_paths:
    print(f"\n Processing: {image_path}")

    gray_img = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if gray_img is None:
        print(f" Error: Unable to load image '{image_path}'")
        continue

    # OCR setup
    custom_config = r'--oem 3 --psm 11'
    ocr_data = pytesseract.image_to_data(gray_img, config=custom_config, output_type=pytesseract.Output.DICT)

    filtered_words = []
    incorrect_words = []

    for i in range(len(ocr_data["text"])):
        word = ocr_data["text"][i].strip()
        try:
            conf = int(ocr_data["conf"][i])
        except ValueError:
            conf = -1
        x, y, w, h = ocr_data["left"][i], ocr_data["top"][i], ocr_data["width"][i], ocr_data["height"][i]

        if len(word) >= 2 and conf > 50 and w > 5 and h > 5:
            corrections = tool.check(word)
            if corrections:
                incorrect_words.append((word, [match.message for match in corrections]))

            cv2.rectangle(gray_img, (x, y), (x + w, y + h), (0, 255, 0), 2)
            cv2.putText(gray_img, word, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.6, (0, 255, 0), 2)
            filtered_words.append(word)
            print(f"Word: '{word}' | Conf: {conf} | Pos: x={x}, y={y}, w={w}, h={h}")

    # Boost contrast and resize
    gray_img = cv2.convertScaleAbs(gray_img, alpha=1.5, beta=10)
    target_width = 400
    scale_factor = target_width / gray_img.shape[1]
    img_re = cv2.resize(gray_img, (0, 0), fx=scale_factor, fy=scale_factor)

    # Show result
    cv2.imshow(f"OCR: {image_path}", img_re)
    cv2.waitKey(0)
    cv2.destroyAllWindows()

    # Spelling error summary
    if incorrect_words:
        print("\n Spelling Errors Found:")
        for word, issues in incorrect_words:
            print(f"- {word}: {issues}")
    else:
        print("\n✅ No spelling errors detected!")
