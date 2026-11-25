package dev.rean.abyss.bonds;
import java.util.UUID;
public final class BondSelfTest {
 static int checks; static void check(boolean ok){checks++;if(!ok)throw new AssertionError();} static void rejects(Runnable r){checks++;try{r.run();}catch(IllegalArgumentException|IllegalStateException e){return;}throw new AssertionError();}
 public static void main(String[] args){UUID a=UUID.randomUUID(),b=UUID.randomUUID(),c=UUID.randomUUID();Bond request=Bond.request(a,b);rejects(()->Bond.request(a,a));rejects(()->request.accept(a));rejects(()->request.accept(c));check(request.decline(b)==null);Bond friends=request.accept(b);check(friends.state()==Bond.State.FRIENDS);Bond proposal=friends.propose(b);check(proposal.decline(a).equals(friends));rejects(()->proposal.accept(b));Bond partners=proposal.accept(a);check(partners.state()==Bond.State.PARTNERS);Bond marriage=partners.propose(a);check(marriage.decline(b).equals(partners));Bond married=marriage.accept(b);check(married.state()==Bond.State.MARRIED);rejects(()->married.propose(a));rejects(()->married.authorizeEnd(c));married.authorizeEnd(b);System.out.println("Bond checks passed: "+checks);}
}
