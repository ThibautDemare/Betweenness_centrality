#!/bin/bash

#Script which can generates different gnuplot script automatically in order to compare results.
#It uses parameters like name of data file.

function rank {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	xlabel=$(sed '1!d' ./DGS_and_results/$nameOfGraph/rank/$nameOfFile.param)
	ylabel=$(sed '2!d' ./DGS_and_results/$nameOfGraph/rank/$nameOfFile.param)
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
	echo "set output \"./rank/$nameOfFile.svg\"" >> ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
	echo "set xlabel '$xlabel'" >> ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
	echo "set ylabel '$ylabel'" >> ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
	echo "set title 'Repartition des nœuds en fonction des rangs obtenus\\n($associatedNameOfGraph)'" >> ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/rank/$nameOfFile\"" >> ./DGS_and_results/Gnuplot/rank_$nameOfFile.gnuplot
}

function repartition1 {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	xlabel=$(sed '1!d' ./DGS_and_results/$nameOfGraph/repartition1/$nameOfFile.param)
	ylabel=$(sed '2!d' ./DGS_and_results/$nameOfGraph/repartition1/$nameOfFile.param)
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
	echo "set output \"./repartition1/$nameOfFile.svg\"" >> ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
	echo "set xlabel '$xlabel'" >> ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
	echo "set ylabel '$ylabel'" >> ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
	echo "set title 'Diagramme de répartition\\n($associatedNameOfGraph)'" >> ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/repartition1/$nameOfFile\"" >> ./DGS_and_results/Gnuplot/repartition1_$nameOfFile.gnuplot
}

function repartition2 {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	xlabel=$(sed '1!d' ./DGS_and_results/$nameOfGraph/repartition2/$nameOfFile.param)
	y1label=$(sed '2!d' ./DGS_and_results/$nameOfGraph/repartition2/$nameOfFile.param)
	y2label=$(sed '3!d' ./DGS_and_results/$nameOfGraph/repartition2/$nameOfFile.param)
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set output \"./repartition2/$nameOfFile.svg\"" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set xlabel '$xlabel'" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set ylabel '$y1label'" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set y2label '$y2label'" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set title 'Diagramme de répartition\\n($associatedNameOfGraph)'" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "set key below width 10" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	#echo "set key noautotitle columnhead" >> ./DGS_and_results/Gnuplot/$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/repartition2/$nameOfFile\" using 1:2 axes x1y1,\\" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
	echo "     \"../$nameOfGraph/repartition2/$nameOfFile\" using 1:3 axes x1y2" >> ./DGS_and_results/Gnuplot/repartition2_$nameOfFile.gnuplot
}

function repartition3 {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	xlabel=$(sed '1!d' ./DGS_and_results/$nameOfGraph/repartition3/$nameOfFile.param)
	y1label=$(sed '2!d' ./DGS_and_results/$nameOfGraph/repartition3/$nameOfFile.param)
	y2label=$(sed '3!d' ./DGS_and_results/$nameOfGraph/repartition3/$nameOfFile.param)
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "set output './repartition3/$nameOfFile.svg'" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "set ylabel '$y1label'" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "set y2label '$y2label'" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "set title 'Diagramme de répartition\\n($associatedNameOfGraph)'" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "set key below width 10" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	#echo "set key noautotitle columnhead" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/repartition3/$nameOfFile\" using 1:2 axes x1y1,\\" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
	echo "     \"../$nameOfGraph/repartition3/$nameOfFile\" using 1:3 axes x1y2" >> ./DGS_and_results/Gnuplot/repartition3_$nameOfFile.gnuplot
}

function simple {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	xlabel=$(sed '1!d' ./DGS_and_results/$nameOfGraph/simple/$nameOfFile.param)
	ylabel=$(sed '2!d' ./DGS_and_results/$nameOfGraph/simple/$nameOfFile.param)
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
	echo "set output './simple/$nameOfFile.svg'" >> ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
	echo "set xlabel '$xlabel'" >> ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
	echo "set ylabel '$ylabel'" >> ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
	echo "set title 'Diagramme de répartition\\n($associatedNameOfGraph)'" >> ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/simple/$nameOfFile\"" >> ./DGS_and_results/Gnuplot/simple_$nameOfFile.gnuplot
}

function correlation {
	nameOfGraph=$1
	nameOfFile=$2
	associatedNameOfGraph=$3
	
	echo "set terminal svg size 1300, 800" > ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set output \"./correlation/$nameOfFile.svg\"" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set yrange [0:1]" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set xrange [0:30]" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set grid ytics" >> ./DGS_and_results/Gnuplot/$nameOfFile.gnuplot
	echo "set style fill solid 1 border -1" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set boxwidth 0.5" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set xtics \(\"$nameOfGraph \(1 000 nœuds\)\" 5, \"$nameOfGraph \(5 000 nœuds\)\" 15, \"$nameOfGraph \(10 000 nœuds\)\" 25\)" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set ylabel \"Coefficient de corrélation\" font \"Bold,14\"" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set xlabel \"Graphes utilisés\" font \"Bold,14\"" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set title \"Coefficient de corrélation entre les différentes mesures pour des tailles de graphe différentes\\n($associatedNameOfGraph)\" font \"Bold,16\"" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "set key below width 10" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	#echo "set key autotitle columnhead" >> ./DGS_and_results/Gnuplot/$nameOfFile.gnuplot
	echo "plot \"../$nameOfGraph/correlation/$nameOfFile\" using \(\$1-2.5\)\:\(\$2\) with boxes lc rgb \"#5fbcd3\",\\" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "	  \"../$nameOfGraph/correlation/$nameOfFile\" using \(\$1-1.5\)\:\(\$3\) with boxes lc rgb \"#0044aa\",\\" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "	  \"../$nameOfGraph/correlation/$nameOfFile\" using \(\$1+1.5\)\:\(\$4\) with boxes lc rgb \"#ff6600\",\\" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
	echo "	  \"../$nameOfGraph/correlation/$nameOfFile\" using \(\$1+2.5\)\:\(\$5\) with boxes lc rgb \"#ffc500\"" >> ./DGS_and_results/Gnuplot/correlation_$nameOfFile.gnuplot
}

#namesOfGraph[0]="DorogovtsevMendes"
#namesOfGraph[1]="Grid"
#namesOfGraph[2]="PreferentialAttachement"
#namesOfGraph[3]="SmallWorld"
namesOfGraph[0]="Villes"

#associatedNamesOfGraph[0]="Dorogovtsev-Mendes"
#associatedNamesOfGraph[1]="Grille simple"
#associatedNamesOfGraph[2]="Attachement préférentiel"
#associatedNamesOfGraph[3]="Petit monde"
associatedNamesOfGraph[0]="Le Havre"

taille=${#associatedNamesOfGraph[@]}
i=0
while [ "$i" -lt "$taille" ]
do
#	for nameOfFile in $(ls -I *.param ${namesOfGraph[$i]}/rank);
#	do
#		rank ${namesOfGraph[$i]} $nameOfFile ${associatedNamesOfGraph[$i]}
#	done
	
#	for nameOfFile in $(ls -I *.param ${namesOfGraph[$i]}/repartition1);
#	do
#		repartition1 ${namesOfGraph[$i]} $nameOfFile ${associatedNamesOfGraph[$i]}
#	done
	
	for nameOfFile in $(ls -I *.param ./DGS_and_results/${namesOfGraph[$i]}/repartition2);
	do
		repartition2 ${namesOfGraph[$i]} $nameOfFile ${associatedNamesOfGraph[$i]}
	done
	
#	for nameOfFile in $(ls -I *.param ${namesOfGraph[$i]}/repartition3);
#	do
#		repartition3 ${namesOfGraph[$i]} $nameOfFile ${associatedNamesOfGraph[$i]}
#	done
	
#	for nameOfFile in $(ls -I *.param ${namesOfGraph[$i]}/simple);
#	do
#		simple ${namesOfGraph[$i]} $nameOfFile ${associatedNamesOfGraph[$i]}
#	done
	
	#for nameOfFile in $(ls -I *.param $namesOfGraph[$i]/dispersion);
	#do
	#	distribution
	#done
	
	#for nameOfFile in $(ls -I *.param $namesOfGraph[$i]/correlation);
	#do
	#	correlation 
	#done
	
	let "i++"
done

cd ./DGS_and_results/Gnuplot
for script in $(ls *.gnuplot)
do
	gnuplot $script
done
rm *.gnuplot
