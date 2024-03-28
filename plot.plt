#!/usr/bin/env gnuplot

# comenzi de rulat in terminal:
# sudo apt-get install gnuplot
# which gnuplot
# gnuplot --version
#
# gnuplot file.plt

set datafile separator ","
set xlabel "YearBuilt"
set ylabel "GrLivArea"
set zlabel "SalePrice" offset -5,0,0
splot 'datasets/houseds.csv' using "YearBuilt":"GrLivArea":"SalePrice" with points, 11 + 12 * x + 13 * y
