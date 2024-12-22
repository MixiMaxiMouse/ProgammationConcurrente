package prodcons.v6;

public class Message {
	String msg;
	Producer p;

	// for debug add ID of thread and buffer status in msg

	public Message(String msg, Producer p) {
		this.msg = msg;
		this.p = p;
	}

	public String getMessage() {
		return msg;
	}

	public void setMessage(String newMsg) {
		this.msg = newMsg;
	}
}
