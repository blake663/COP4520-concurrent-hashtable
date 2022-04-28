import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt

filename = "experimentalTimes.csv"

df = pd.read_csv(filename)


print(df["Unthreaded"])