# import the library
import sumolib
import sys
# parse the net
#Argumentos
#1: ID de nodo o Junction
#2: Path del archivo .net
#3: i - incoming y o - outgoing edges
def main():
    net = sumolib.net.readNet(sys.argv[2])
    if str(sys.argv[3]) == "o":
        nodo = net.getNode(str(sys.argv[1])).getOutgoing()
    elif str(sys.argv[3]) == "i":
        nodo = net.getNode(str(sys.argv[1])).getIncoming()
    cadena = ""
    for i in nodo:
        cadena = cadena + str(i).rsplit("\"")[1] + ";"
    print(cadena)


if __name__ == "__main__":
    main()
