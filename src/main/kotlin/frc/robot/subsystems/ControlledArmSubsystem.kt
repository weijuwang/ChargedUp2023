package frc.robot.subsystems

import edu.wpi.first.wpilibj2.command.SubsystemBase
import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel.MotorType
import com.revrobotics.RelativeEncoder
import com.revrobotics.SparkMaxPIDController
import frc.robot.Constants.Arm

class ControlledArmSubsystem(ArmConsts: Arm): SubsystemBase() {
    val motor: CANSparkMax = CANSparkMax(ArmConsts.motorPort, MotorType.kBrushless)
    val encoder: RelativeEncoder = motor.getEncoder()
    val pidcontroller: SparkMaxPIDController = motor.getPIDController()

    init {
        pidcontroller.setP(ArmConsts.kp)
        pidcontroller.setI(ArmConsts.ki)
        pidcontroller.setD(ArmConsts.kd)
        pidcontroller.setIZone(ArmConsts.kiz)
        pidcontroller.setFF(ArmConsts.kff)
        pidcontroller.setOutputRange(ArmConsts.kminoutput, ArmConsts.kmaxoutput)
    }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    fun moveAmount(radians:Double){
        //rotates x amount of radians, assuming 1 rotation = 2p radians
        pidcontroller.setReference(radians/(2*Math.PI), CANSparkMax.ControlType.kPosition)
    }

    fun moveToGoal(goalPos: Double){
        //sets position to a given amount of radians; hopefully it works
        val current: Double = encoder.getPosition() * (2*Math.PI)
        val toMove: Double = (
            if (current > goalPos)
                ( Math.abs(current - goalPos) / (2*Math.PI) )
            else
                ( current - goalPos / (2*Math.PI) )
        )
        pidcontroller.setReference(toMove, CANSparkMax.ControlType.kPosition)
    }


    
}
