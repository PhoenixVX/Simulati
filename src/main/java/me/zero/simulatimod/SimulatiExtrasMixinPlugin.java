package me.zero.simulatimod;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class SimulatiExtrasMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("extramixins.floats")) {
            return SimulatiMod.getConfig().extras.fixDoubleFloatCasts;
        } else {
            return true;
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (mixinClassName.contains("extramixins.floats")) {
            // Handle float casting
            handleFloatCasting(targetClassName, targetClass, mixinClassName, mixinInfo);
        }
    }

    public void handleFloatCasting(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // TODO: Make ASM code actually work
        if (true) {
            return;
        }

        targetClass.methods.parallelStream().forEach(methodNode -> {
            // Iterate over instructions until we find a double-float cast
            ListIterator<AbstractInsnNode> iter = methodNode.instructions.iterator();
            boolean shortCircuit = false;
            int index = 0;
            int nodesBeforeF2DCounter = 0;
            List<AbstractInsnNode> nodesBeforeF2D = new ArrayList<>();
            while (iter.hasNext() && !shortCircuit) {
                AbstractInsnNode abstractInsnNode = iter.next();
                index++;
                if (abstractInsnNode != null) {
                    // Locate int to float
                    if (abstractInsnNode.getOpcode() == Opcodes.I2F) {
                        // Push number to stack
                        AbstractInsnNode ldcNode = iter.next();
                        if (ldcNode != null && ldcNode.getOpcode() == Opcodes.LDC) {
                            // Locate double conversion
                            if (abstractInsnNode.getOpcode() == Opcodes.F2D) {
                                methodNode.instructions.set(abstractInsnNode, new VarInsnNode(Opcodes.I2D, index));
                                for (AbstractInsnNode abstractInsnNode1 : nodesBeforeF2D) {
                                    nodesBeforeF2DCounter++;
                                    if (abstractInsnNode1 != null) {
                                        if (abstractInsnNode1.getOpcode() == Opcodes.FADD) {
                                            methodNode.instructions.set(abstractInsnNode1, new InsnNode(Opcodes.DADD));
                                        } else if (abstractInsnNode1.getOpcode() == Opcodes.FSUB) {
                                            methodNode.instructions.set(abstractInsnNode1, new InsnNode(Opcodes.DSUB));
                                        } else if (abstractInsnNode1.getOpcode() == Opcodes.FMUL) {
                                            methodNode.instructions.set(abstractInsnNode1, new InsnNode(Opcodes.DMUL));
                                        } else if (abstractInsnNode1.getOpcode() == Opcodes.FDIV) {
                                            methodNode.instructions.set(abstractInsnNode1, new InsnNode(Opcodes.DDIV));
                                        }
                                    }

                                    if (nodesBeforeF2DCounter >= 20) {
                                        break;
                                    }
                                }

                                shortCircuit = true;
                            } else {
                                nodesBeforeF2D.add(abstractInsnNode);
                                methodNode.instructions.add(abstractInsnNode);
                            }
                        } else {
                            methodNode.instructions.add(ldcNode);
                        }
                    } else {
                        methodNode.instructions.add(abstractInsnNode);
                    }
                }
            }
        });
    }
}
