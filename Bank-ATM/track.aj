
public aspect Track {
	pointcut callTrack(): call (* BankSystem.Track);
	before():callTrack(){
		System.out.println("Before Deposit")
	}
	after():callTrack(){
		System.out.println("After Deposit")
	}

}
