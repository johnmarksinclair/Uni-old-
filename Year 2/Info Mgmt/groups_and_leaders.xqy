for $b in doc("ScoutLeader.xml")/scout_leaders/scout_leader
  return <scout_leader> 
  { $b/scout_group }
  { $b/ group_location }
  { $b/name }
  </scout_leader>