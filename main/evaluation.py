import platform
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

importedCPU = True
try:
	import cpuinfo
except:
	importedCPU = False

filename = "experimentalTimes.csv"
df = pd.read_csv(filename)
linespace = range(1, len(df) + 1)
names = df.columns
rows, cols = 2, 2
index = 0

if (importedCPU):
	print(cpuinfo.cpu.info[0]["ProcessorNameString"])
else:
	print("Could not record CPU information")
	

print("Trials: " + str(len(df)))
for name in names:
	print("{0}:\n\tAverage: {1} ms\n\tHigh: {2} ms\n\tLow: {3} ms\n\tStandard deviation: {4} ms".
		format(name, round(df[name].mean()), df[name].max(), df[name].min(), round(df[name].std(), 1)))

fig, axs = plt.subplots(ncols = cols, nrows = rows)
for col in range(cols):
	for row in range(rows):
		sns.scatterplot(data = df, x = linespace, y = names[index], ax = axs[col][row]).set(title = names[index], ylabel = "Time (ms)")
		axs[col][row].set_ylim(0, df[names[index]].max() * 1.1)
		index += 1
fig.suptitle("Time Taken For {0} Tests".format(str(len(df))), fontsize = 15)
plt.tight_layout()
plt.show()