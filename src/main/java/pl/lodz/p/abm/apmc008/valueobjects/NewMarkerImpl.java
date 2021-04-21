/*
 * Author: Łukasz Słodownik.
 * Integral part of the bachelor thesis "Analiza i prezentacja danych pomiarowych z systemu motion capture".
 * Supervisor of the bachelor thesis: Ph.D.Michał Ludwicki, Lodz University of Technology.
 * Copyright © 2020 Łukasz Słodownik.
 */

package pl.lodz.p.abm.apmc008.valueobjects;


import java.util.*;

// TODO: test this class
public final class NewMarkerImpl implements Marker {
    private final String id;
    private final String name;
    private final MarkerType markerType;
    private final CoordinateType coordinateType;
    private final NavigableMap<Integer, CoordinatePair> coordinatePairMap;
    private final NavigableMap<Integer, Coordinate> coordinatePositionMap;
    private final NavigableMap<Integer, Coordinate> coordinateRotationMap;

    public NewMarkerImpl(
            String id,
            String name,
            MarkerType markerType,
            CoordinateType coordinateType,
            Optional<Map<Integer, Coordinate>> coordinatePositionOptionalMap,
            Optional<Map<Integer, Coordinate>> coordinateRotationOptionalMap
    ) {
        if (coordinatePositionOptionalMap.isEmpty() && coordinateRotationOptionalMap.isEmpty())
            throw new RuntimeException("At least one optional should be present");

        if (coordinateType.equals(CoordinateType.Both) && (coordinatePositionOptionalMap.isEmpty() || coordinateRotationOptionalMap.isEmpty()))
            throw new RuntimeException("Both optionals should be present");

        this.id = id;
        this.name = name;
        this.markerType = markerType;
        this.coordinateType = coordinateType;

        switch (coordinateType) {
            case Position: {
                this.coordinatePositionMap = new TreeMap<>(coordinatePositionOptionalMap.get());
                this.coordinateRotationMap = null;
                this.coordinatePairMap = null;
                break;
            }
            case Rotation: {
                this.coordinatePositionMap = null;
                this.coordinateRotationMap = new TreeMap<>(coordinateRotationOptionalMap.get());
                this.coordinatePairMap = null;
                break;
            }
            case Both: {
                this.coordinatePositionMap = null;
                this.coordinateRotationMap = null;
                this.coordinatePairMap = populatedCoordinatePairMap(coordinatePositionOptionalMap,coordinateRotationOptionalMap);
                break;
            }
            default:
                throw new RuntimeException("Unknown coordinate");
        }
    }

    private NavigableMap<Integer, CoordinatePair> populatedCoordinatePairMap(Optional<Map<Integer, Coordinate>> coordinatePositionOptionalMap, Optional<Map<Integer, Coordinate>> coordinateRotationOptionalMap) {
        final TreeMap<Integer, CoordinatePair> resultMap = new TreeMap<>();
        final var positionMap = coordinatePositionOptionalMap.get();
        final var rotationMap = coordinateRotationOptionalMap.get();
        final Iterator<Integer> iteratorOverPositionKeySet = positionMap.keySet().iterator();
        final Iterator<Integer> iteratorOverRotationKeySet = rotationMap.keySet().iterator();
        int firstFrame;
        if (iteratorOverPositionKeySet.hasNext())
            firstFrame = iteratorOverPositionKeySet.next();
        else if (iteratorOverRotationKeySet.hasNext())
            firstFrame = iteratorOverRotationKeySet.next();
        else
            return resultMap;

        int lastFrame = firstFrame;

        while (iteratorOverPositionKeySet.hasNext()) {
            final int currentFrame = iteratorOverPositionKeySet.next();

            if (currentFrame < firstFrame)
                firstFrame = currentFrame;

            if (currentFrame > lastFrame)
                lastFrame = currentFrame;
        }

        while (iteratorOverRotationKeySet.hasNext()) {
            final int currentFrame = iteratorOverRotationKeySet.next();

            if (currentFrame < firstFrame)
                firstFrame = currentFrame;

            if (currentFrame > lastFrame)
                lastFrame = currentFrame;
        }

        for (int i = firstFrame; i <= lastFrame; ++i){
            final Coordinate positionCoordinate = positionMap.get(i);
            final Coordinate rotationCoordinate = rotationMap.get(i);
            if (positionCoordinate == null && rotationCoordinate == null)
                continue;

            final CoordinatePair coordinatePair = new CoordinatePair(positionCoordinate, rotationCoordinate);
            resultMap.put(coordinatePair.getFrame(),coordinatePair);
        }

        return resultMap;
    }


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MarkerType getMarkerType() {
        return this.markerType;
    }

    @Override
    public CoordinateType getCoordinateType() {
        return this.coordinateType;
    }

    @Override
    public Iterator<Integer> getFrameIterator(CoordinateType requestedCoordinateType) {
        if (!validateRequestedCoordinateType(requestedCoordinateType))
            throw new RuntimeException("CoordinateType does not match");


        final NavigableSet<Integer> integers = getCoordinateMap(requestedCoordinateType).navigableKeySet();
        switch (coordinateType){
            case Position: case Rotation: return getCoordinateMap(requestedCoordinateType).navigableKeySet().iterator();
            case Both: return new MarkerFrameAdapterIterator(this.coordinatePairMap.entrySet().iterator(), requestedCoordinateType);
            default: throw new RuntimeException();
        }
    }


    @Override
    public Iterator<Map.Entry<Integer, Coordinate>> getEntrySetIterator(CoordinateType requestedCoordinateType) {
        if (!validateRequestedCoordinateType(requestedCoordinateType))
            throw new RuntimeException("CoordinateType does not match");

        switch (this.coordinateType){
            case Position: case Rotation: return getCoordinateMap(requestedCoordinateType).entrySet().iterator();
            case Both: return new MarkerEntrySetAdapterIterator(this.coordinatePairMap.entrySet().iterator(),requestedCoordinateType);
            default: throw new RuntimeException();
        }

    }

    @Override
    public int getFirstFrameNumber(CoordinateType reqCoordinateType) {
        if (!validateRequestedCoordinateType(reqCoordinateType))
            throw new RuntimeException("CoordinateType does not match");

        switch (this.coordinateType){
            case Position: case Rotation: return getCoordinateMap(reqCoordinateType).firstKey();
            case Both: {
                Coordinate coordinate = getFirstCoordinateFromComplexMap(reqCoordinateType);
                return coordinate.getFrame();
            }
            default: throw new RuntimeException();
        }
    }

    private Coordinate getFirstCoordinateFromComplexMap(CoordinateType reqCoordinateType) {
        for (Map.Entry<Integer, CoordinatePair> entry : this.coordinatePairMap.entrySet()) {
            final Coordinate coordinate = entry.getValue().getCoordinate(reqCoordinateType);
            if (coordinate != null)
                return coordinate;
        }
        return null;
    }

    @Override
    public int getLastFrameNumber(CoordinateType requestedCoordinateType) {
        if (!validateRequestedCoordinateType(coordinateType))
            throw new RuntimeException("CoordinateType does not match");

        switch (this.coordinateType){
            case Position: case Rotation: return getCoordinateMap(requestedCoordinateType).lastKey();
            case Both: return getLastCoordinateFromComplexMap(requestedCoordinateType).getFrame();
            default: throw new RuntimeException();
        }
    }

    private Coordinate getLastCoordinateFromComplexMap(CoordinateType requestedCoordinateType) {
        for (Map.Entry<Integer, CoordinatePair> entry : this.coordinatePairMap.descendingMap().entrySet()) {
            final Coordinate coordinate = entry.getValue().getCoordinate(requestedCoordinateType);
            if (coordinate != null)
                return coordinate;
        }
        return null;
    }

    @Override
    public Coordinate getCoordinate(Integer key, CoordinateType reqCoordinateType){
        if (!validateRequestedCoordinateType(coordinateType))
            throw new RuntimeException("CoordinateType does not match");

        switch (this.coordinateType){
            case Rotation: case Position: return getCoordinateMap(reqCoordinateType).get(key);
            case Both: return this.coordinatePairMap.get(key).getCoordinate(reqCoordinateType);
            default: throw new RuntimeException("Unknown coordinate type");
        }
    }

    @Override
    public RangeOfCoordinates getRangeOfCoordinates(
            int firstFrame,
            int lastFrame,
            CoordinateType requestedCoordinateType
    ) {
        if (!validateRequestedCoordinateType(coordinateType))
            throw new RuntimeException("CoordinateType does not match");

        final boolean isFKey, isLKey;
        switch (this.coordinateType) {
            case Position:
            case Rotation:
                return createRangeOfCoordinatesFromSimpleTreeMap(firstFrame, lastFrame, requestedCoordinateType);
            case Both:
                return createRangeOfCoordinatesFromComplexTreeMap(firstFrame,lastFrame,requestedCoordinateType);
            default:
                throw new RuntimeException();
        }
    }

    @Override
    public RangeOfCoordinates getRangeOfCoordinates(CoordinateType requestedCoordinateType) {
        return getRangeOfCoordinates(this.getFirstFrameNumber(requestedCoordinateType),this.getLastFrameNumber(requestedCoordinateType),requestedCoordinateType);
    }

    private RangeOfCoordinates createRangeOfCoordinatesFromComplexTreeMap(int firstFrame, int lastFrame, CoordinateType requestedCoordinateType) {
        if (!validateRequestedCoordinateType(coordinateType))
            throw new RuntimeException("CoordinateType does not match");

        final boolean isFKey, isLKey;
        isFKey = this.coordinatePairMap.containsKey(firstFrame);
        isLKey = this.coordinatePairMap.containsKey(lastFrame);
        if (isFKey && isLKey && firstFrame < lastFrame){
            final NavigableMap<Integer, CoordinatePair> integerCoordinatePairNavigableMap = this.coordinatePairMap.subMap(firstFrame, true, lastFrame, true);
            final ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>(integerCoordinatePairNavigableMap.size());
            switch (requestedCoordinateType){
                case Position: integerCoordinatePairNavigableMap.forEach((integer, coordinatePair) -> coordinates.add(coordinatePair.getPositionCoordinate())); break;
                case Rotation: integerCoordinatePairNavigableMap.forEach((integer, coordinatePair) -> coordinates.add(coordinatePair.getRotationCoordinate())); break;
                default: throw new RuntimeException();
            }
            coordinates.trimToSize();
            return new RangeOfCoordinates(firstFrame, lastFrame, this.id, this.name, requestedCoordinateType, Collections.unmodifiableList(coordinates));
        }
        throw new FramesNotFoundException(isFKey,isLKey);
    }

    private RangeOfCoordinates createRangeOfCoordinatesFromSimpleTreeMap(int firstFrame, int lastFrame, CoordinateType requestedCoordinateType) {
        final boolean isFKey, isLKey;
        isFKey = getCoordinateMap(requestedCoordinateType).containsKey(firstFrame);
        isLKey = getCoordinateMap(requestedCoordinateType).containsKey(lastFrame);
        if (isFKey && isLKey && firstFrame < lastFrame){
            final ArrayList<Coordinate> coordinates = new ArrayList<>(getCoordinateMap(requestedCoordinateType).subMap(firstFrame, true, lastFrame, true).values());
            coordinates.trimToSize();
            return new RangeOfCoordinates(firstFrame, lastFrame, this.getId(),this.getName(),requestedCoordinateType,coordinates);
        }

        throw new FramesNotFoundException(isFKey,isLKey);
    }


    private boolean validateRequestedCoordinateType(CoordinateType requestedCoordinateType) {
        if (requestedCoordinateType == null)
            throw new RuntimeException("Null as CoordinateType");

        if (this.coordinateType.equals(CoordinateType.Both)) {
            if (requestedCoordinateType.equals(CoordinateType.Both))
                throw new RuntimeException("CoordinateType.Both is wrong argument");

            return true;
        }

        return this.coordinateType.equals(requestedCoordinateType);
    }


    private NavigableMap<Integer, Coordinate> getCoordinateMap(CoordinateType coordinateType) {
        switch (coordinateType) {
            case Position:
                return this.coordinatePositionMap;
            case Rotation:
                return this.coordinateRotationMap;
            default:
                throw new RuntimeException("Unknown CoordinateType");
        }
    }

}