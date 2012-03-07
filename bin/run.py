import os
import subprocess
names = ['12','40']
namesNumCats = dict({
	'12':[8,12,16,20],
	'40':[30,35,40,45]
})
numIters =[1000,2000]
	
cmd = "bash" 

f="resulatsAuto.txt"

for name in names:
	for cats in namesNumCats[name]:
		print "name: "+name+" namesNumCats: "+ str(cats)
		os.putenv('M_NUMTOPICS',str(cats))
		subprocess.call([cmd, "./bin/commandsEval.sh",name,f],stderr=subprocess.STDOUT)
	

		  
