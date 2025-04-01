package pl.pz1.problem.visitors.identifier;

/**
 * The Identifier class represents an identifier with a unique ID and name.
 * It is designed to distinguish between different roles, such as writers and readers.
 */
public class Identifier {
    /**
     * Constant representing the writer role.
     */
    public static final String WRITER = "Pisarz";

    /**
     * Constant representing the reader role.
     */
    public static final String READER = "Czytelnik";

    private final Integer id;
    private final String name;
    private final String shortName;

    /**
     * Constructs an Identifier with a specified ID and name.
     *
     * @param id   the unique identifier
     * @param name the name associated with the identifier
     */
    public Identifier(int id, String name) {
        this.id = id;
        this.name = name;
        if (name.equals(WRITER)) {
            this.shortName = "W";
        }
        else {
            this.shortName = "R";
        }
    }

    /**
     * Returns the name associated with this Identifier.
     *
     * @return the name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique ID of this Identifier.
     *
     * @return the ID as an Integer
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the short name associated with this Identifier.
     *
     * @return the short name as a String
     */
    public String getShortName() {
        return shortName;
    }
}

