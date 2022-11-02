import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/restaurant">
      Restaurant
    </MenuItem>
    <MenuItem icon="asterisk" to="/services">
      Services
    </MenuItem>
    <MenuItem icon="asterisk" to="/image-services">
      Image Services
    </MenuItem>
    <MenuItem icon="asterisk" to="/category">
      Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/pay">
      Pay
    </MenuItem>
    <MenuItem icon="asterisk" to="/invoice">
      Invoice
    </MenuItem>
    <MenuItem icon="asterisk" to="/detail-invoice">
      Detail Invoice
    </MenuItem>
    <MenuItem icon="asterisk" to="/comment">
      Comment
    </MenuItem>
    <MenuItem icon="asterisk" to="/hotel">
      Hotel
    </MenuItem>
    <MenuItem icon="asterisk" to="/room-hotel">
      Room Hotel
    </MenuItem>
    <MenuItem icon="asterisk" to="/type-room">
      Type Room
    </MenuItem>
    <MenuItem icon="asterisk" to="/price-room">
      Price Room
    </MenuItem>
    <MenuItem icon="asterisk" to="/detail-invoice-room">
      Detail Invoice Room
    </MenuItem>
    <MenuItem icon="asterisk" to="/image-room">
      Image Room
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
