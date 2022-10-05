import socket


def fun():
    conn, address = sock.accept()
    print('Connect with ' + address[0] + ':' + str(address[1]))

    all_data = ''
    while True:
        data = conn.recv(1024)
        if data:
            all_data += data.decode('utf-8')  # same result with new_data.decode()
            print(all_data[2:])
            if all_data[2:] == 'quit':
                break
            fun()
    conn.close()


HOST = socket.gethostname()
print(socket.gethostbyname(HOST))

sock = socket.socket()
print('Socket created!')
sock.bind(('', 8000))
print('Socket bound!')
sock.listen(1)
print('Socket is now listening...')
fun()
sock.close()
