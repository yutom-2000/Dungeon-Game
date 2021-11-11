#represents a single town
#name = name of town
#character = character occupying the town
#adjacencyList = list of adjacent towns
class town:
    def __init__(self, name, character, adjacencyList):
        self.name = name
        self.character = character
        self.adjacencyList = adjacencyList

    def __eq__(self, other):
        if (isinstance(other, self.__class__)):
            return other.name == self.name
        else: return False

#represents network of towns
#holds list of towns
class network:
    def __init__(self, townList):
        self.townList = townList

    #return True if added, false if already contained
    def addTown(self, T):
        if (T in self.townList) :
            return False
        else:
            self.townList.append(T)
            return True

    #Return true if removed, false if no change
    def removeTown(self, T):
        if (T in self.townList):
            self.townList.remove(T)
            return True
        else:
            return False;

    #Return true if town is empty
    #Returns true if town does not exist
    def isTownEmpty(self, T):
        if (T in self.townList):
            node = self.townList.get(self.townList.index(T))
            return node.character is None
        else:
            return True

    #Return true if town is included in network
    def doesTownExist(self, T):
        return (T in self.townList)


    def doesRoadExist(self, T1, T2):
        if (T1 in self.townList):
            if (T2 in self.townList):
                t1 = self.townList.get(self.townList.index(T1))
                t2 = self.townList.get(self.townList.index(T2))
                return T1 in t2.adjacencyList and T2 in t1.adjacencyList

    def addRoad(self, T1, T2):
        if (self.doesRoadExist(T1, T2)):
            return False
        else:
            t1 = self.townList.get(self.townList.index(T1))
            t2 = self.townList.get(self.townList.index(T2))
            t1.adjacencyList.append(t2)
            t2.adjacencyList.append(t1)
            return True

    def removeRoad(self, T1, T2):
        if (self.doesRoadExist(T1, T2)):
            t1 = self.townList.get(self.townList.index(T1))
            t2 = self.townList.get(self.townList.index(T2))
            t1.adjacencyList.remove(t2)
            t2.adjacencyList.remove(t1)
            return True
        else: return False

    def doesCharacterExist(self, c):
        for T in self.townList:
            if T.character == c:
                return True
        return False

    def addCharacter(self, c, T):
        if (self.doesTownExist(T) and self.isTownEmpty(T)):
            t = self.townList.get(self.townList.index(T))
            t.character = c
            return True
        else: False

    def removeCharacter(self, c):
        if (self.doesCharacterExist(c)):
            for T in self.townList:
                if T.character == c:
                    T.character = None
                    return True
        return False

    def canReachTown(self, c, T):
        if (self.doesCharacterExist(c)):
            for T in self.townList:
                if T.character == c:
                    branches = T.adjacencyList
                    while True:
                        b = []
                        for n in branches:
                            if (self.isTownEmpty(n)):
                                b.append(n)
                        if (T in b):
                            return True
                        else:
                            branches = b
        else: return False
