for i in dot*.bfma dot*.mama dot*.pama dot*.wima
do
	dot -Tpng -o $i.png $i;
done
