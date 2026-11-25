package dev.rean.abyss.bonds;
import java.util.Objects;
import java.util.UUID;
/** Immutable, consent-based relationship state. Persistence and commands are separate. */
public record Bond(UUID first, UUID second, State state, UUID proposer) {
 public enum State { FRIEND_REQUEST, FRIENDS, PARTNER_REQUEST, PARTNERS, MARRIAGE_REQUEST, MARRIED }
 public Bond { Objects.requireNonNull(first); Objects.requireNonNull(second); Objects.requireNonNull(state); if(first.equals(second)) throw new IllegalArgumentException("Self relationship"); boolean pending=state==State.FRIEND_REQUEST||state==State.PARTNER_REQUEST||state==State.MARRIAGE_REQUEST; if(pending ? !first.equals(proposer)&&!second.equals(proposer) : proposer!=null) throw new IllegalArgumentException("Invalid proposer"); }
 public static Bond request(UUID from, UUID to){return new Bond(from,to,State.FRIEND_REQUEST,from);}
 private void member(UUID actor){if(!first.equals(actor)&&!second.equals(actor))throw new IllegalArgumentException("Not a participant");}
 public Bond propose(UUID actor){member(actor); return new Bond(first,second,switch(state){case FRIENDS->State.PARTNER_REQUEST;case PARTNERS->State.MARRIAGE_REQUEST;default->throw new IllegalStateException("Cannot propose now");},actor);}
 public Bond accept(UUID actor){member(actor); if(actor.equals(proposer))throw new IllegalArgumentException("Cannot accept own request");return new Bond(first,second,switch(state){case FRIEND_REQUEST->State.FRIENDS;case PARTNER_REQUEST->State.PARTNERS;case MARRIAGE_REQUEST->State.MARRIED;default->throw new IllegalStateException("No pending request");},null);}
 public Bond decline(UUID actor){member(actor);return switch(state){case FRIEND_REQUEST->null;case PARTNER_REQUEST->new Bond(first,second,State.FRIENDS,null);case MARRIAGE_REQUEST->new Bond(first,second,State.PARTNERS,null);default->throw new IllegalStateException("No pending request");};}
 public void authorizeEnd(UUID actor){member(actor);}
}
