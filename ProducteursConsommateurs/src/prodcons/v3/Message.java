package prodcons.v3;

public class Message {
	String msg;

	// for debug add ID of thread and buffer status in msg

	public Message(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String newMsg) {
		this.msg = newMsg;
	}
}
