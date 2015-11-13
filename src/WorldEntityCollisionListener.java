import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class WorldEntityCollisionListener implements ContactListener {



    @Override
    public void beginContact(Contact contact) {
        WorldEntity.collidedEntities(contact.getFixtureA().getBody().hashCode(),
                                     contact.getFixtureB().getBody().hashCode());
    }



    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold manifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
