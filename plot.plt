#!/usr/bin/env gnuplot

A = -0.46562730905168315
B = 13.606240746925756
C = 101.46591826674094

set datafile separator ","
set xlabel "YearBuilt"
set ylabel "GrLivArea"
set zlabel "SalePrice" offset -5,0,0
splot 'datasets/houseds.csv' using "YearBuilt":"GrLivArea":"SalePrice" with points, A + B * x + C * y
