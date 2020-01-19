pointOneXY = [0, 0]
pointTwoXY = [400, 400]
pointThreeXY = [200, 0]
pointFourXY = [0, 400]

lineOne = [pointOneXY, pointTwoXY]
lineTwo = [pointThreeXY, pointFourXY]

def direction(pointOne, pointTwo, pointThree):
    value = (pointTwo[1] - pointOne[1]) * (pointThree[0] - pointTwo[0]) - (pointTwo[0] - pointOne[0]) * (pointThree[1]-pointTwo[1])
    print(value)
    if value == 0:
        return 0 # colinear
    if value < 0:
        return 2 # anti clockwise
    return 1 # clockwise

def onLine(line, point):
    if point[0] <= max(line[0][0], line[1][0]) and point[0] <= min(line[0][0], line[1][0]) and point[1] <= max(line[0][1], line[1][1]) and point[1] <= min(line[0][1], line[1][1]):
        return True
    return False

def isIntersect(lineOne, lineTwo):
    dirOne = direction(pointOneXY, pointTwoXY, pointThreeXY)
    dirTwo = direction(pointOneXY, pointTwoXY, pointFourXY)
    dirThree = direction(pointThreeXY, pointFourXY, pointOneXY)
    dirFour = direction(pointThreeXY, pointFourXY, pointTwoXY)

    print(str(dirOne) + " " + str(dirTwo) + " " + str(dirThree) + " " + str(dirFour))

    if dirOne != dirTwo and dirThree != dirFour:
        print("HI")
        return True
    if dirOne == 0 and onLine(lineOne, pointThreeXY):
        print("HI1")
        return True
    if dirTwo == 0 and onLine(lineOne, pointFourXY):
        print("HI2")
        return True
    if dirThree == 0 and onLine(lineTwo, pointOneXY):
        print("HI3")
        return True
    if dirFour == 0 and onLine(lineTwo, pointTwoXY):
        print("HI4")
        return True
    return False


import time
current = time.time()

if isIntersect(lineOne, lineTwo):
    print("Well done boss")
else:
    print("Try better boss")


from tkinter import *
window = Tk()
c = Canvas(window, width=400, height = 400, bg='blue')
c.pack()

c.create_line(pointOneXY[0], pointOneXY[1], pointTwoXY[0], pointTwoXY[1], fill='green')
c.create_line(pointThreeXY[0], pointThreeXY[1], pointFourXY[0], pointFourXY[1], fill='green')

print(time.time() - current)


    
