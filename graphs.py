# Author: Steve Devoy
# Purpose: Code samples. NDAs prevent me from sharing code developed for employers and clients.

from enum import Enum

# CostEvaulator is intended as an abstract class.
# Instantiable classes should be derived from it.
class CostEvaluator:
    def __init__(self, args):
        self.args = args
        
    def evaluate(self, data):
        """Subclasses should evaluate the data and return a numerical value
        representing the cost of the data as a whole.
        """
        return 0
# end class CostEvaluator
    
class SimpleCostEvaluator(CostEvaluator):
    def __init__(self, args):
        super().__init__(args)
        self.key = args[0]
        
    def evaluate(self, data):
        """Return the value in the data corresponding to the key provided
        when this instance was created.
        """
        return data[self.key]
# end class SimpleCostEvaluator

class CostComparitor:
    def __init__(self, costEvaluator):
        self.costEvaluator = costEvaluator
        
    def compare(self, path1, path2):
        """Used to compare the costs of two paths.
        Returns a numeric value less than zero when path1 costs more than path2.
        Returns a numeric value greater than zero when path1 costs less than path2.
        Returns 0 when path1 and path2 have equal cost.
        """
        
        path1Value = self.costEvaluator(path1.data)
        path2Value = self.costEvaluator(path2.data)
        return path1Value - path2Value
# end class CostComparitor

        
class DataAggregator:
    def __init__(self, args):
        """ Receives a list of keys.
        Subclasses should override this constructor such that subsequent calls to
        aggregate retrieve the data needed and then act upon that data to return
        a new data set based on two data sets.
        """
        self.args = args
        return

    def aggregate(self, data, aggregatedData):
        """ Return a new data dictionary that represents the aggregation
        of the data in the two dictionary parameters.
        This method is a stub and should be overridden by subclasses.
        """
        return data
# end class DataAggregator

class SimpleDataAggregator(DataAggregator):
    def __init__(self, args):
        """ Receives a list of keys of length one.
        This key is stored for retrieval of the value within the data dictionary
        of a path corresponding to this single key.
        """
        super().__init__(args)
        self.key = args[0]
        
    def aggregate(self, data1, data2):
        """ Given data sets data1 and data2, this method extracts the value
        corresponding to the key stored in key from each of data1 and data2,
        sums the values, and creates a new data dictionary containing the sum
        as the value of key.
        """
        data1Value = data1[self.key]
        data2Value = data2[self.key]
        return { self.key: data1Value + data2Value}
# end class DataAggregator

class Node:
    def __init__(self, label = "", payload = None):
        """Constructs a Node instance with empty incoming and outgoing lists,
        self.label set to label, and self.payload set to payload.
        Currently, payload is not in use, but it is intended to be put into use
        in future versions of this class. It will hold data relevant to this
        specific node, for example, the cost of transiting the node.
        """
        self.incoming = []
        self.outgoing = []
        self.label = label
        self.payload = payload
        
    def __str__(self):
        """Returns a string representation of this node in human readable form."""
        name = "?" if (self.label == None) else self.label
        asString = name + ":\n  outgoing:"
        
        for edge in self.outgoing:
            asString += "\n    " + str(edge)
        
        asString += "\n  incoming:"
        
        for edge in self.incoming:
            asString += "\n    " + str(edge)
            
        return asString
    
    def addEdge(self, edge):
        """ Adds edge to node. If the edge starts at this node, it adds
        the node to the outgoing list of edges. If the edge ends at this node,
        it adds the node to the incoming list of edges. If the edge neither
        starts nor ends at this node, it is ignored and None is returned.
        """
        if edge is None:
            return None
        elif edge.start is self:
            if edge in self.outgoing:
                return None
            else:
                self.outgoing.append(edge)
        elif edge.end is self:
            if edge in self.incoming:
                return None
            else:
                self.incoming.append(edge)
        else:
            return None
        
        return edge
    
    def removeEdge(self, edge):
        """Removes edge from either the incoming or outgoing lists of edges
        for this node, depending upon whether the edge starts at this node
        or ends at this node.
        """
        if edge is None:
            return None 
        
        if edge.start == self:
            if (len(self.outgoing) == 0):
                return None 
            elif edge in self.outgoing:
                self.outgoing.remove(edge)
                return edge 
            else:
                return None 
            
        if edge.end == self:
            if (len(self.incoming) == 0):
                return None 
            elif edge in self.incoming:
                self.incoming.remove(edge)
                return edge
            else:
                return None 
        
        return None
# end class Node

class Edge:
    def __init__(self, start = None, end = None, data = None, label = ""):
        """Constructs a new instance of Edge."""
        self.start = start
        self.end = end
        self.data = data
        self.label = label
        
    def __str__(self):
        """Returns a human readable representation of this Edge."""
        startLabel = "None" if (self.start is None) else self.start.label
        endLabel = "None" if (self.end is None) else self.end.label
        
        if (self.data is None):
            return "" + startLabel + "->" + endLabel
        else:
            return "" + startLabel + "-(" + str(self.data) + ")->" + endLabel
# end class Edge

# Currently not used.
PathType = Enum('PathType', ['SIMPLE', 'DISTANCE', 'TIME', 'COMPUTED'])
    
class Path:
    def __init__(self, newEdge = None, oldPath = None):
        """Constructor for Path instances.
        Given a new edge and an old path, it returns a new path extending
        the old path.
        """
        self.pathType = PathType.SIMPLE
        
        self.end = newEdge
        self.rest = oldPath        
        self.data = None
            
        if (oldPath is None):
            self.edgeCount = 1
        else:
            self.edgeCount = oldPath.edgeCount
        
        if (not oldPath is None):
            self.nodes = oldPath.nodes.copy()
        else:
            self.nodes = set()
            
        self.nodes.add(newEdge.start)
        
    def __str__(self):
        """ Returns a human readable representation of this path."""
        if self.rest is None:
            if self.end is None:
                return ""
            else:
                return "" + str(self.end)
        else:
            return "" + str(self.rest) + ", " + str(self.end)
        
    def __lt__(self, other):
        if(self.edgeCount < other.edgeCount):
            return True
        else:
            return False
        
    def __le__(self, other):
        if(self.edgeCount <= other.edgeCount):
            return True
        else:
            return False
        
    def __gt__(self, other):
        if(self.edgeCount > other.edgeCount):
            return True
        else:
            return False
        
    def __ge__(self, other):
        if(self.edgeCount >= other.edgeCount):
            return True
        else:
            return False
        
    def __eq__(self, other):
        if(self.edgeCount == other.edgeCount):
            return True
        else:
            return False
        
    def reset(self):
        """Resets all instance variables for reuse of the Path instance."""
        self.end = None 
        self.rest = None 
        self.data = None 
        self.edgeCount = 1
        self.nodes = self()
        
    def clear(self):
        """Clears all data in the instance variables for this instance."""
        self.end = None 
        self.rest = None 
        self.data = None 
        self.edgeCount = 10
        self.nodes = None 
        
    def contains(self, e):
        """Traverses the path to find edge e. If e is along the path,
        True is returned. If e is not along the path, False is returned.
        Used to prevent circular paths.
        """
        if e is None:
            return False 
        elif isinstance(e, Node):
            return (e in self.nodes) if (not self.nodes is None) else False
        elif isinstance(e, Edge):
            if self.rest is None:
                return False 
            else:
                return e in self.rest
        else:
            return False
    
    def countEdges(self):
        """Sanity check for self.edgeCount.
        self.edgeCount is set when the path is created, based on the previous path.
        countEdges, instead, counts the edges in the path one by one.
        This method exists primarily for debugging purposes as it is
        an inefficient way to measure the length of a path. However, unlike
        self.edgeCount, it does not depend upon the constructor working
        properly.
        """
        if self.rest is None:
            return 1
        else:
            return 1 + self.rest.countEdges()
        
# end class Path

class PathWithCost(Path):
    def __init__(self, newEdge = None, oldPath = None,
                 costEvaluator = SimpleCostEvaluator(["distance"]),
                 aggregator = SimpleDataAggregator(["distance"])):
        """ Create a path that allows cost to be associated."""
        self.pathType = PathType.COMPUTED        
        self.end = newEdge
        self.rest = oldPath
        self.costEvaluator = costEvaluator
        self.aggregator = aggregator
        
        if ((not aggregator is None) and (not newEdge is None)):
            if (oldPath is None):
                self.data = newEdge.data
            else:
                self.data = aggregator.aggregate(newEdge.data, oldPath.data)
        else:
            self.data = None
            
        if (oldPath is None):
            self.edgeCount = 1
        else:
            self.edgeCount = oldPath.edgeCount
        
        if (not oldPath is None):
            self.nodes = oldPath.nodes.copy()
        else:
            self.nodes = set()
            
        self.nodes.add(newEdge.start)
        
    def cost(self):
        """Return the cost of the edge."""
        return self.costEvaluator.evaluate(self.data) if bool(self.data) else self.edgeCount
        
    def __lt__(self, other):
        e1 = self.data;
        e2 = other.data;
        
        if e1 is None:
            if e2 is None:
                return False 
            else:
                return True 
        elif e2 is None:
            return True;

        d1 = self.costEvaluator(e1)
        d2 = self.costEvaluator(e2)
        
        return d1 < d2
        
    def __le__(self, other):
        e1 = self.data;
        e2 = other.data;
        
        if e1 is None:
            if e2 is None:
                return False 
            else:
                return True 
        elif e2 is None:
            return True;

        d1 = self.costEvaluator(e1)
        d2 = self.costEvaluator(e2)
        
        return d1 <= d2
        
    def __gt__(self, other):
        e1 = self.data;
        e2 = other.data;
        
        if e1 is None:
            if e2 is None:
                return False 
            else:
                return True 
        elif e2 is None:
            return True;

        d1 = self.costEvaluator(e1)
        d2 = self.costEvaluator(e2)
        
        return d1 > d2
        
    def __ge__(self, other):
        e1 = self.data;
        e2 = other.data;
        
        if e1 is None:
            if e2 is None:
                return False 
            else:
                return True 
        elif e2 is None:
            return True;

        d1 = self.costEvaluator(e1)
        d2 = self.costEvaluator(e2)
        
        return d1 >= d2
        
    def __eq__(self, other):
        e1 = self.data;
        e2 = other.data;
        
        if e1 is None:
            if e2 is None:
                return False 
            else:
                return True 
        elif e2 is None:
            return True;

        d1 = self.costEvaluator(e1)
        d2 = self.costEvaluator(e2)
        
        return d1 == d2
# end class PathWithCost

class SearchState:
    def __init__(self, edge, path):
        self.edge = edge
        self.path = path
        
    def __str__(self):
        return "<|State: edge=" + str(self.edge) + ", path=" + str(self.path) + "|>"
        
# end SearchState

# Vertices will be provided as a list of all vertex labels.
# Edges will be provided as a list of dictionaries.
#   Each edge entry has the following keys: "from", "to", "payload". payload
#   may be None.
# Vertices are processed before edges.

class Graph:
    def __init__(self):
        self.vertexLabels = set()   # Set
        self.edgeData = []          # List of dictionaries
        self.nodeLookup = {}        # Dictionary
        self.allPaths = []          # List
        
    def create(self, vertexLabels, edgeData):
        for vertexLabel in vertexLabels:
            if not vertexLabel in self.nodeLookup:
                self.nodeLookup[vertexLabel] = Node(vertexLabel)
                
        for data in edgeData:
            edgeStartNodeLabel = data["from"]
            edgeEndNodeLabel = data["to"]
            edgePayload = data["payload"]
            
            edgeStartNode = self.nodeLookup[edgeStartNodeLabel]
            edgeEndNode = self.nodeLookup[edgeEndNodeLabel]
            
            # Do error checking here...
            
            newEdge = Edge(edgeStartNode, edgeEndNode, edgePayload)            
            edgeStartNode.addEdge(newEdge)
            edgeEndNode.addEdge(newEdge)
    
        for nodeKey in self.nodeLookup:
            node = self.nodeLookup[nodeKey]
            print(str(node) + "\n")
            
    def getAllPaths(self, fromLabelOrNode, toLabelOrNode):
        """ Returns all paths from the orgin to the destination.
        fromLabelOrNode can be either a node or the label of the origin node.
        toLabelOrNode can be either a node or the label of the destination node.
        The return value is a list of paths.
        """
        fromNode = None 
        toNode = None 
        
        if isinstance(fromLabelOrNode, str):
            fromNode = self.nodeLookup[fromLabelOrNode]
        elif isinstance(fromLabelOrNode, Node):
            fromNode = fromLabelOrNode

        if isinstance(toLabelOrNode, str):
            toNode = self.nodeLookup[toLabelOrNode]
        elif isinstance(toLabelOrNode, Node):
            toNode = toLabelOrNode
            
        print("fromNode=")
        print(fromNode)
        
        print("\ntoNode=")
        print(toNode)
            
        allPathsAsList = []
        
        if fromNode is None or toNode is None:
            return allPathsAsList
        
        allEdges = []
        workingSearchState = []
        outgoing = fromNode.outgoing
        currentPath = None
        
        if (fromNode == toNode):
            allPathsAsList.append(Path())
            
        if (not outgoing is None):        
            for edge in outgoing:
                workingSearchState.append(SearchState(edge, None))
                print("\nworkingSearchState=")
                for s in workingSearchState:
                    print(str(s))
                
        while bool(workingSearchState):
            state = workingSearchState.pop(0)
            workingEdge = state.edge
            previousPath = state.path
            
            if workingEdge in allEdges:
                continue
            
            currentPath = Path(workingEdge, previousPath)
            
            if workingEdge.end is toNode:
                allPathsAsList.append(currentPath)
            else:
                nextNode = workingEdge.end
                
                if currentPath.contains(nextNode):
                    continue
                
                outgoing = nextNode.outgoing
                
                if bool(outgoing):
                    for e in outgoing:
                        workingSearchState.append(SearchState(e, currentPath))
                        
        return allPathsAsList
    
    def getOptimalPath(self, fromLabelOrNode, toLabelOrNode):
        """ Returns the path from the origin to the destination with the lowest cost.
        fromLabelOrNode can be either a node or the label of the origin node.
        toLabelOrNode can be either a node or the label of the destination node.
        The return value is a single path.
        """
        fromNode = None 
        toNode = None 
        shortestPath = None
        
        if isinstance(fromLabelOrNode, str):
            fromNode = self.nodeLookup[fromLabelOrNode]
        elif isinstance(fromLabelOrNode, Node):
            fromNode = fromLabelOrNode

        if isinstance(toLabelOrNode, str):
            toNode = self.nodeLookup[toLabelOrNode]
        elif isinstance(toLabelOrNode, Node):
            toNode = toLabelOrNode
            
        print("fromNode=")
        print(fromNode)
        
        print("\ntoNode=")
        print(toNode)
        
        if (fromNode == toNode):
            return shortestPath

        cycleCount = 0
        shortestPathValue = -1.0
        allEdges = set()
        workingSearchStates = []
        outgoing = fromNode.outgoing
        currentPath = None 
        
        if (bool(outgoing)):
            for e in outgoing:
                workingSearchStates.append(SearchState(e, None))
                
        while bool(workingSearchStates):
            state = workingSearchStates.pop(0)
            workingEdge = state.edge
            previousPath = state.path
            cycleCount += 1
            
            if not workingEdge in allEdges:
                currentPath = PathWithCost(workingEdge, previousPath)
                
                if (workingEdge.end == toNode):
                    currentPathValue = currentPath.cost()
                    
                    if (True if shortestPathValue == -1.0 else currentPathValue < shortestPathValue):
                        shortestPathValue = currentPathValue
                        shortestPath = currentPath
                else:
                    nextNode = workingEdge.end
                    
                    if not currentPath.contains(nextNode):
                        outgoing = nextNode.outgoing
                        
                        if (bool(outgoing)):
                            for e in outgoing:
                                workingSearchStates.append(SearchState(e, currentPath))
                    else:
                        currentPath.clear()
        return shortestPath
                        
                                                 

    def run(self):
        """ Runs the path search using getOptimalPath.
        """
        self.allPaths = self.getOptimalPath("Boston", "Los Angeles")
        return self.allPaths
    

graph = Graph()
vertexLabels = {'Boston', 'New York', 'Chicago',
                'Denver', 'Dallas', 'Los Angeles',
                'Seattle', 'San Francisco'}
edgeData = [{'from': 'Boston', 'to': 'New York', 'payload': {"distance": 306.146}},
            {'from': 'Boston', 'to': 'San Francisco', 'payload': {"distance": 4333.537}},
            {'from': 'Boston', 'to': 'Chicago', 'payload': {"distance": 1365.887}},
            {'from': 'New York', 'to': 'Los Angeles', 'payload': {"distance": 3935.620}},
            {'from': 'New York', 'to': 'Chicago', 'payload': {"distance": 1161.315}},
            {'from': 'New York', 'to': 'San Francisco', 'payload': {"distance": 4128.788}},
            {'from': 'Chicago', 'to': 'Denver', 'payload': {"distance": 1477.602}},
            {'from': 'Chicago', 'to': 'Los Angeles', 'payload': {"distance": 2803.698}},
            {'from': 'Chicago', 'to': 'Dallas', 'payload': {"distance": 1291.406}},
            {'from': 'Denver', 'to': 'Seattle', 'payload': {"distance": 1639.414}},
            {'from': 'Denver', 'to': 'Los Angeles', 'payload': {"distance": 1639.414}},
            {'from': 'Dallas', 'to': 'Los Angeles', 'payload': {"distance": 1991.912}}
            ]
graph.create(vertexLabels, edgeData)
shortestPath = graph.run()

print("\n")

print(shortestPath)
print("\nDistance=" + str(shortestPath.cost()))
