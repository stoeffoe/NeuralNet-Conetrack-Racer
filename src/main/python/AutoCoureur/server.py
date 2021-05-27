import socket as sc

class Server:
    address = '', 50008
    socketType = sc.AF_INET, sc.SOCK_STREAM
    maxNrOfConnectionRequests = 5
    maxMessageLength = 256

    def __init__ (self):
        with sc.socket (*self.socketType) as serverSocket:
            serverSocket.bind (self.address)
            serverSocket.listen (self.maxNrOfConnectionRequests)

            print('Server initiated, listening on %s' % str(self.address))

            while True:
                self.clientSocket, address = serverSocket.accept ()
                print("Connection received from %s..." % str(address))

                with self.clientSocket:
                    while True:
                        incomingString = self.recv ()
                        self.send (incomingString)

    def send (self, message):
        buffer = bytes (f'{message:<{self.maxMessageLength}}', 'ascii')

        totalNrOfSentBytes = 0

        while totalNrOfSentBytes < self.maxMessageLength:
            nrOfSentBytes = self.clientSocket.send (buffer [totalNrOfSentBytes:])

            if not nrOfSentBytes:
                self.raiseConnectionError ()
                
            totalNrOfSentBytes += nrOfSentBytes

    def recv (self):
        totalNrOfReceivedBytes = 0
        receivedChunks = []

        while totalNrOfReceivedBytes < self.maxMessageLength:
            receivedChunk = self.clientSocket.recv (self.maxMessageLength - totalNrOfReceivedBytes)

            if not receivedChunk:
                self.raiseConnectionError ()

            receivedChunks.append (receivedChunk)
            totalNrOfReceivedBytes += len (receivedChunk)
            
        return b''.join (receivedChunks) .decode ('ascii')

    def raiseConnectionError (self):
        raise RuntimeError ('Socket connection broken')


if __name__ == "__main__":
    Server ()                      
