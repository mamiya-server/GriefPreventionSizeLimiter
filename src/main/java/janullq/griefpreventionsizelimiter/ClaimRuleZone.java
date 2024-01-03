package janullq.griefpreventionsizelimiter;
import org.bukkit.World;

public class ClaimRuleZone {
    private final int _lesserX;
    private final int _lesserZ;
    private final int _greaterX;
    private final int _greaterZ;
    private final World _world;
    public int maxAreaSize;
    public String message;
    public ClaimRuleZone(World world, int x1, int z1, int x2, int z2, int maxAreaSize) {
        this._world = world;
        if (x1 <= x2) {
            this._lesserX = x1;
            this._greaterX = x2;
        } else {
            this._lesserX = x2;
            this._greaterX = x1;
        }
        if (z1 <= z2) {
            this._lesserZ = z1;
            this._greaterZ = z2;
        } else {
            this._lesserZ = z2;
            this._greaterZ = z1;
        }
        this.maxAreaSize = maxAreaSize;
        this.message = "";
    }
    public World getWorld() {
        return this._world;
    }
    public int getLesserX() {
        return this._lesserX;
    }
    public int getLesserZ() {
        return this._lesserZ;
    }
    public int getGreaterX() {
        return this._greaterX;
    }
    public int getGreaterZ() {
        return this._greaterZ;
    }
    public boolean isIn(World world, int x, int z) {
        return (world == this._world &&
                x >= this._lesserX && x <= this._greaterX &&
                z >= this._lesserZ && z <= this._greaterZ);
    }
}
