archive_name='PP_Tema_1_Trifan_Bogdan_Cristian_322CDa'

archive:
	rm -f *.zip ~/Downloads/$(archive_name).zip
	zip -r $(archive_name).zip src/main/scala/* build.sbt ID.txt plot.plt
	cp $(archive_name).zip ~/Downloads/

zip: archive

plot:
	gnuplot -p plot.plt

install-plot:
	sudo apt-get install gnuplot

get-plot-version:
	which gnuplot
	gnuplot --version


