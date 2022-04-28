import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

filename = "experimentalTimes.csv"
df = pd.read_csv(filename)
linespace = range(1, len(df) + 1)
names = df.columns
rows, cols = 2, 2
index = 0


fig, axs = plt.subplots(ncols = cols, nrows = rows)
for col in range(cols):
	for row in range(rows):
		sns.scatterplot(data = df, x = linespace, y = names[index], ax = axs[col][row]).set(title = names[index], ylabel = "Time (ms)")
		axs[col][row].set_ylim(0, df[names[index]].max() * 1.1)
		index += 1
plt.subplots_adjust(left = 0.1, bottom = 0.1, right = 0.9, top = 0.9, wspace=.5, hspace=0.5)

plt.show()