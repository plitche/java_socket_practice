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
  



https://www.youtube.com/watch?v=-xKgxqG411c




- 소켓 ( socket )



왜 배우는가?

좀뭔가 멋있어보이는 새로운 개념이 등장했다. 먼저 왜 발명되었는지 장점이 뭔지 좀 체크하자.

우리는 앞서 openStream() 과 같은 메소드로 타입을 바꾸고 그 바뀐타입으로 여러가지 기본적인 통신을 배웠다.

자 그런데 그통신은 사실은 default 값이다. 그러니까 윈도우에서 제공하는 아주 기본적이고 단순한 구조의 통신이라는 것이다.

주어진 도구를 그냥 가져다가쓴다? 뭔가 엔지니어,크리에이터로써는 어울리지않는 행동이다. 그리고 기본적인 통신구조는 잘 알려져있어 보안에도 취약하다 그래서 나만의 통신구조를 설계하는데 그것을 바로 소켓 프로그래밍이라고한다. 우리가 직접 통신 그 근본과정을 일일이 다 설계한다는 것이다.


우리가 서버와 통신을 한다고 가정해보자. 그 서버의 특정 App에 접근을 하기위해서는 특정 port와 연결이 되어야한다. 

해당 port에 가면 우리는 바로 app에 접근하는것이 아니라 소켓을 거친뒤에 app에 접근 & 이용을 한다.

우리가 설계하는 소켓프로그래밍은 이 소켓부분을 설계에서 어떠한 방식으로 통신을 할지 정해주는 것이다.



- 소켓의 통신방식



소켓이 뭐 저런식으로 쓰인다는것은 알지만 교양수준으로 저렇게배워서 어따 써먹겠냐.. 자세히 그 알고리즘을 좀 알아보자.

어떤 단계를 거쳐서 통신이 이루어지는가?


1. 먼저 Client class 를 생성한다. 클래스의 파라미터는 보기와 같다.

2. 해당  클래스에 서버의 ip주소와 port 번호를 넣고 출력 스트림으로 넘어간후 Server Socket에 접근한다.

3. Server Socket class는 client가 접속을 했는지 체크만하는 용도이다.

4. 접근이 옴이 인식되면 서버 클래스는 제빨리 Socket.accept() 메소드를 실행한다.



자 그러면 위와같은 1:1 통신이 아니라 하나의 서버에 여러컴퓨터가 접속해오는 경우는 어떨까?

ServerSocket하나가 여러개 컴퓨터의 client 접속을 인식한다고 생각하면 안된다.

client 1개당 1개의 ServerSocket 이 생성된다. 예를들어 컴퓨터 1000대가 접속하면

1000개의 서버소켓이 만들어진고 1000개의 서버소켓이 1000개의 accept() 메소드를 호출하는 것이다...음....상당히 비효율적이다.

서버 소켓이 너무많아지면 굳이 실습을하지않아도 상당히 과부하가 걸릴것으로 예상된다.

이 문제를 해결하기위해서 나온것이 바로 쓰레드(Thread) 라는 것이다.  통신에 상당히자주 쓰이는것으로 나중에 학습할것이다. 
