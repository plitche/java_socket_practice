# java_socket_practice
1. create Client Socket  

```java
Socket socket = new Socket()
```  
* 서버 연결  
> 서버쪽의 ip랑 port를 인자로 넣은 SocketAddress 프로퍼티를 만들고 이것을 인자로 넣는 connect 함수를 사용  
 
```java
SocketAddress address = new InetSocketAddress(hostName, port);
socket.connect(address);
```  
  
  
2. create Person Object
```java
class Person implements Serializable {
    int age;

    Person(int age) {
        this.age= age;
    }
}
```
- Serializable를 implements 해줘야 나중에 byte로 변환(직렬화)이 가능
  
  
3. send Object to Server as Socket after convert byte  
 - 자바 직렬화는 방법은 java.io.ObjectOutputStream 객체를 이용  

```java
public static byte[] toByteArray (Object obj)
{
    byte[] bytes = null;
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        bytes = bos.toByteArray();
    }
    catch (IOException ex) {
        //TODO: Handle the exception
    }
    return bytes;
}
```  
이제 해당 함수로 받아온 byte array를 소켓을 통해 전달  
```java
byte[] data = toByteArray(person); //생성한 person 객체를 byte array로 변환
OutputStream os = socket.getOutputStream(); //서버로 내보내기 위한 출력 스트림 뚫음
os.write(data); //출력 스트림에 데이터 씀
os.flush(); //전달
```  
  
  
4. Server에서 받은 byte를 그대로 Client에게 돌려 보내면, 해당 byte를 다시 Person Object로 변환  
서버(아래에 해당 코드가 있어요)는 위에서 받은 byte array를 그대로 돌려보낸다.  
  
서버에서 보내는 바이트를 받는 코드
```java
int maxBufferSize = 1024; //수신 버퍼의 최대 사이즈 지정
byte[] recvBuffer = new byte[maxBufferSize]; //버퍼 생성
InputStream is = socket.getInputStream(); //서버로부터 받기 위한 입력 스트림 뚫음
int nReadSize = is.read(recvBuffer); //버퍼(recvBuffer) 인자로 넣어서 받음. 반환 값은 받아온 size
```
> 돌려 받은 byte array를 편하게 사용하려면 다시 Person 객체로 돌려놔야겠죠?(역직렬화)

사실 서버 코드에서 socket, stream 등등 전역변수로 설정하고 작업을 다 끝내고 나면열어놓은 다 close() 함수를 통해 닫아줘야한다.
하지만 가독성과 설명을 위해 전역변수가 아닌 지역변수로 변경
byte 단위로 작업을 처리해야했고, 한번이 아니라 계속 api 쏘면서 확인하고 싶었기 때문에

> close 안할 시 Address already in use: JVM_Bind 에러 발생 가능함
