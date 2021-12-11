# -*- coding: utf-8 -*-
"""
Created on Fri May 28 23:29:35 2021

@author: ManavChordia
"""

import json

with open("C:\\Users\\ManavChordia\\Work\\MiniProject\\CLX_Android\\NoFall.json") as f:
  data = json.load(f)

fall = []

for i in data:
    fall.append(i)
    
fall_clubbed = []

temp = [fall[0]]
for i in range(1, len(fall)):
    
    if int(fall[i]) - int(fall[i-1]) == 1:
        temp.append(fall[i])
    else:
        fall_clubbed.append(temp)
        temp = []
        temp.append(fall[i])
        
fall_data = []

temp = []
reading = []
for i in fall_clubbed:
    temp = []
    for j in i:
        for k in data[j]:
            reading = []
            cnt = 0
            for l in data[j][k]:
                reading.append(data[j][k][l])
            temp.append(reading)
    fall_data.append(temp)
    
acc_data = []
gyro_data = []   
for i in fall_data:
    temp_a = []
    temp_g = []
    cnt = 0
    for j in i:
        if cnt == 0:
            temp_a.append(j)
            cnt = 1
        else:
            temp_g.append(j)
            cnt = 0
    
    acc_data.append(temp_a)
    gyro_data.append(temp_g)
    
    
for i in range(len(acc_data)):
    for j in range(len(acc_data[i])):
        for k in range(len(acc_data[i][j])):
            acc_data[i][j][k] = float(acc_data[i][j][k])
    
min_a = []   
for i in acc_data:
    temp = []
    for j in range(3):
        res = [min(k) for k in zip(*i)][j]
        temp.append(res)
        
    min_a.append(temp)
    
max_a = []   
for i in acc_data:
    temp = []
    for j in range(3):
        res = [max(k) for k in zip(*i)][j]
        temp.append(res)
        
    max_a.append(temp)
    
import numpy as np
    
min_a = np.array(min_a)
max_a = np.array(max_a)

acc_final_fall = np.concatenate((min_a, max_a), axis = 1)

for i in range(len(gyro_data)):
    for j in range(len(gyro_data[i])):
        for k in range(len(gyro_data[i][j])):
            gyro_data[i][j][k] = float(gyro_data[i][j][k])
            
            
min_g = []   
for i in gyro_data:
    temp = []
    for j in range(3):
        res = [min(k) for k in zip(*i)][j]
        temp.append(res)
        
    min_g.append(temp)
    
max_g = []   
for i in gyro_data:
    temp = []
    for j in range(3):
        res = [max(k) for k in zip(*i)][j]
        temp.append(res)
        
    max_g.append(temp)
    
import numpy as np
    
min_g = np.array(min_g)
max_g = np.array(max_g)

gyro_final_fall = np.concatenate((min_g, max_g), axis = 1)
        

Nofall_final = np.concatenate((acc_final_fall, gyro_final_fall), axis = 1)

x_fall = fall_final
with open("C:\\Users\\ManavChordia\\Work\\MiniProject\\CLX_Android\\NoFall.json") as f:
  datA = json.load(f)
  
  
X = np.concatenate((x_fall, Nofall_final), axis = 0)

Y = [1]*13 + [0]*19

Y = np.array(Y)

import sklearn
from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split( X, Y, test_size=0.2, random_state=42)

from sklearn.ensemble import RandomForestClassifier
clf = RandomForestClassifier(max_depth=10, random_state=0)
clf.fit(X_train, y_train)

print(clf.predict(X_test))