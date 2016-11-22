
/**
 * In this version 1.1, the scanned event is more 
 * intelligent than the version 1.0. It's know where
 * the enemies are and can lock to the target and shoot 
 * according to the distance.  
 * 
 * @author HTOO AUNG HLAING (56103012-3) 
 * @version 1.1
 * 
 */

package fnl;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.awt.Color;
import robocode.*;

public class MyBot extends AdvancedRobot
{
   final int DISTANCE = 50; // distance when robot hit by bullet
   int direction = 1; // direction to move when robot hit wall

   public void run()
   {
      // My Tank Color
      setBodyColor(Color.white);
      setGunColor(Color.black);
      setRadarColor(Color.orange);
      setBulletColor(Color.red);
      setScanColor(Color.red);

      // setting the gun and radar move freely from robot
      setAdjustRadarForRobotTurn(true);
      setAdjustGunForRobotTurn(true);
      keepTurningRadarRightForTarget();
   }

   public void onScannedRobot(ScannedRobotEvent e)
   {
      lockRadarToTarget();

      // Getting the absolute location of enemies
      double absBearing = getLocationOfEnemy(e);

      // How to turn gun to the target
      double gunTurnAmt = getGunTurnAmount(absBearing);

      // If enemy is far, do this
      if (e.getDistance() > 100)
      {
         setTurnGunRightRadians(gunTurnAmt); // get gun target to the enemy
         setAhead((e.getDistance() / 2) * direction); // move half distance to
                                                      // get more
         // accuracy
         setFire(1); // fire

      }
      else
      {
         setTurnGunRightRadians(gunTurnAmt); // get gun target to the enemy
         setAhead((e.getDistance() / 2) * direction); // move half distance to
                                                      // get more accuracy
         setFire(3); // shot with big one!!!
      }

   }

   // Utility functions

   private double getGunTurnAmount(double absBearing)
   {
      return (robocode.util.Utils
            .normalRelativeAngle(absBearing - getGunHeadingRadians()));
   }

   private void keepTurningRadarRightForTarget()
   {
      turnRadarRightRadians(Double.POSITIVE_INFINITY);
   }

   private void lockRadarToTarget()
   {
      // This function locks the radar to the target
      setTurnRadarLeftRadians(getRadarTurnRemainingRadians());
   }

   private double getLocationOfEnemy(ScannedRobotEvent e)
   {
      return (e.getBearingRadians() + getHeadingRadians());
   }
   
   // When robot hit by bullet do this 
   public void onHitByBullet(HitByBulletEvent e)
   {

      setAhead(DISTANCE); // distance to move when my robot hit by bullet
      turnLeft(90);

   }
   
   // When robot hit the wall do this 
   public void onHitWall(HitWallEvent e)
   {
      direction = -direction; // move reverse direction when hit wall
   }
   
   // When robot hit with enemies do this 
   public void onHitRobot(HitRobotEvent e)
   {
      // Fire Hard when hit with others robots
      double gunTurnForHit = normalRelativeAngleDegrees(
            e.getBearing() + getHeading() - getGunHeading());
      turnGunRight(gunTurnForHit);
      fire(3);
   }
}
