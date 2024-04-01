#!/usr/bin/env gnuplot

# comenzi de rulat in terminal:
# sudo apt-get install gnuplot
# which gnuplot
# gnuplot --version
#
# gnuplot -p plot.plt

set datafile separator ","
set xlabel "YearBuilt"
set ylabel "GrLivArea"
set zlabel "SalePrice" offset -5,0,0
splot 'datasets/houseds.csv' using "YearBuilt":"GrLivArea":"SalePrice" with points, 0.1403710327315429 + 59.54116952531836 * x + 41.47822698064429 * y
