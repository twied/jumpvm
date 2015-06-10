all: report.pdf slides.pdf

report.pdf: report.tex report.bib
	pdflatex report.tex
	bibtex report.aux
	pdflatex report.tex
	pdflatex report.tex

slides.pdf: slides.tex
	pdflatex slides.tex
	pdflatex slides.tex
