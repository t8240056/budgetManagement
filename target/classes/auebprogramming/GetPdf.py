import requests

url = "https://minfin.gov.gr/wp-content/uploads/2024/11/%CE%9A%CF%81%CE%B1%CF%84%CE%B9%CE%BA%CF%8C%CF%82-%CE%A0%CF%81%CE%BF%CF%8B%CF%80%CE%BF%CE%BB%CE%BF%CE%B3%CE%B9%CF%83%CE%BC%CF%8C%CF%82-2025_%CE%9F%CE%95.pdf"
#response = requests.get(url)

#with open("budget2025.pdf", "wb") as f:
    #f.write(response.content)


from pdfminer.high_level import extract_text
import tabula

# Εξαγωγή όλων των πινάκων από PDF
tables = tabula.read_pdf("budget2025.pdf", pages='all')
