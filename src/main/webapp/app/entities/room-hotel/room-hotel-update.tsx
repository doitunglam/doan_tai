import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITypeRoom } from 'app/shared/model/type-room.model';
import { getEntities as getTypeRooms } from 'app/entities/type-room/type-room.reducer';
import { getEntity, updateEntity, createEntity, reset } from './room-hotel.reducer';
import { IRoomHotel } from 'app/shared/model/room-hotel.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const RoomHotelUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const typeRooms = useAppSelector(state => state.typeRoom.entities);
  const roomHotelEntity = useAppSelector(state => state.roomHotel.entity);
  const loading = useAppSelector(state => state.roomHotel.loading);
  const updating = useAppSelector(state => state.roomHotel.updating);
  const updateSuccess = useAppSelector(state => state.roomHotel.updateSuccess);
  const handleClose = () => {
    props.history.push('/room-hotel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTypeRooms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...roomHotelEntity,
      ...values,
      typeRoom: typeRooms.find(it => it.id.toString() === values.typeRoom.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...roomHotelEntity,
          typeRoom: roomHotelEntity?.typeRoom?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="doantaiApp.roomHotel.home.createOrEditLabel" data-cy="RoomHotelCreateUpdateHeading">
            Create or edit a RoomHotel
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="room-hotel-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Room Name" id="room-hotel-roomName" name="roomName" data-cy="roomName" type="text" />
              <ValidatedField label="Description" id="room-hotel-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Room Status" id="room-hotel-roomStatus" name="roomStatus" data-cy="roomStatus" type="text" />
              <ValidatedField id="room-hotel-typeRoom" name="typeRoom" data-cy="typeRoom" label="Type Room" type="select">
                <option value="" key="0" />
                {typeRooms
                  ? typeRooms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/room-hotel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RoomHotelUpdate;
